
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_logs.ActivityLog;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int activityLogId = super.getRequest().getData("id", int.class);
		Optional<ActivityLog> activityLog = this.repository.findByIdAndFlightCrewMemberId(activityLogId, flightCrewMember.getId());

		boolean status = activityLog.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Collection<ActivityLog> activityLogs;

		activityLogs = this.repository.findActivityLogsByCrewMemberId(flightCrewMember.getId());

		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);

		super.bindObject(activityLog, "incidentType", "incidentDescription", "severityLevel", "flightAssignment");
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
		activityLog.setDraftMode(true);

		super.unbind(activityLog);
	}
}
