
package acme.features.flightCrewMember.activityLog;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int activityLogId = super.getRequest().getData("id", int.class);
		Optional<ActivityLog> opt = this.repository.findActivityLogById(activityLogId);

		boolean status = false;
		if (opt.isPresent()) {
			ActivityLog log = opt.get();
			FlightAssignment assignment = log.getFlightAssignment();

			if (assignment != null && assignment.getAllocatedFlightCrewMember().getId() == userId && assignment.getStatus().equals(AssignmentStatus.CONFIRMED) && !assignment.isDraftMode() && log.isDraftMode())
				status = true;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int activityLogId = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(activityLogId).get();
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "incidentType", "incidentDescription", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "incidentDescription", "severityLevel", "draftMode");
		dataset.put("activityLogId", activityLog.getId());
		dataset.put("memberId", super.getRequest().getPrincipal().getActiveRealm().getId());
		super.getResponse().addData(dataset);
	}
}
