
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_logs.ActivityLog;
import acme.entities.flight_assignments.AssignmentStatus;
import acme.entities.flight_assignments.FlightAssignment;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getMethod().equals("POST"))
			if (super.getRequest().hasData("flightAssignment")) {
				int assignmentId = super.getRequest().getData("flightAssignment", int.class);
				if (assignmentId != 0) {
					Optional<FlightAssignment> optionalAssignment = this.repository.findFlightAssignmentById(assignmentId);
					if (optionalAssignment.isEmpty() || !optionalAssignment.get().getStatus().equals(AssignmentStatus.CONFIRMED) || optionalAssignment.get().getAllocatedFlightCrewMember().getId() != userId || optionalAssignment.get().isDraftMode())
						status = false;
				}
			}
		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {

		int activityLogId;
		ActivityLog activityLog;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId).get();

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {

		super.bindObject(activityLog, "incidentType", "incidentDescription", "severityLevel", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "incidentDescription", "severityLevel", "flightAssignment", "draftMode");

		Collection<FlightAssignment> flightAssignments = this.repository.findConfirmedFlightAssignmentsByCrewMemberId(flightCrewMember.getId());

		FlightAssignment selected = activityLog.getFlightAssignment();
		if (selected != null && !flightAssignments.contains(selected))
			flightAssignments.add(selected);

		SelectChoices assignmentChoices = SelectChoices.from(flightAssignments, "id", activityLog.getFlightAssignment());
		dataset.put("assignments", assignmentChoices);

		dataset.put("activityLogId", activityLog.getId());
		dataset.put("memberId", super.getRequest().getPrincipal().getActiveRealm().getId());

		super.getResponse().addData(dataset);
	}
}
