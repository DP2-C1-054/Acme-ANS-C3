
package acme.features.flightCrewMember.activityLog;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_logs.ActivityLog;
import acme.entities.flight_assignments.AssignmentStatus;
import acme.entities.flight_assignments.FlightAssignment;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		Integer flightAssignmentId = 0;
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getMethod().equals("POST")) {
			if (super.getRequest().hasData("flightAssignment")) {
				int assignmentId = super.getRequest().getData("flightAssignment", int.class);
				if (assignmentId != 0) {
					Optional<FlightAssignment> optionalAssignment = this.repository.findFlightAssignmentById(assignmentId);
					if (optionalAssignment.isEmpty() || optionalAssignment.get().isDraftMode() || !optionalAssignment.get().getStatus().equals(AssignmentStatus.CONFIRMED) || optionalAssignment.get().getAllocatedFlightCrewMember().getId() != userId)
						status = false;
				}
			}
		}

		else {
			if (super.getRequest().hasData("id"))
				flightAssignmentId = super.getRequest().getData("id", int.class);

			if (this.repository.findFlightAssignmentById(flightAssignmentId).get().getAllocatedFlightCrewMember().getId() != userId)
				status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		FlightAssignment flightAssignment = null;

		Integer assignmentId = null;
		if (super.getRequest().hasData("id"))
			assignmentId = super.getRequest().getData("id", Integer.class);

		if (assignmentId != null && assignmentId > 0)
			flightAssignment = this.repository.findFlightAssignmentById(assignmentId).orElse(null);

		ActivityLog activityLog = new ActivityLog();
		activityLog.setDraftMode(true);
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setFlightAssignment(flightAssignment);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		int assignmentId;

		super.bindObject(activityLog, "incidentType", "incidentDescription", "severityLevel");

		if (super.getRequest().hasData("flightAssignment")) {
			assignmentId = super.getRequest().getData("flightAssignment", int.class);
			FlightAssignment assignment = this.repository.findFlightAssignmentById(assignmentId).orElse(null);
			activityLog.setFlightAssignment(assignment);
		}
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "incidentDescription", "severityLevel", "draftMode");

		FlightAssignment assignment = activityLog.getFlightAssignment();
		if (assignment != null)
			dataset.put("flightAssignment", assignment.getId());
		else
			dataset.put("flightAssignment", 0);

		dataset.put("activityLogId", activityLog.getId());
		dataset.put("memberId", super.getRequest().getPrincipal().getActiveRealm().getId());

		super.getResponse().addData(dataset);
	}

}
