
package acme.features.flightCrewMember.activityLog;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_logs.ActivityLog;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		int activityLogId = super.getRequest().getData("id", int.class);
		Optional<ActivityLog> activityLog = this.repository.findByIdAndFlightCrewMemberId(activityLogId, flightCrewMember.getId());
		super.getResponse().setAuthorised(activityLog.isPresent());
	}

	@Override
	public void load() {
		int activityLogId = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(activityLogId).get();
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		ActivityLog managed = this.repository.findActivityLogById(activityLog.getId()).orElse(null);
		if (managed != null)
			this.repository.delete(managed);
	}

	@Override
	public void bind(final ActivityLog object) {
		;
	}

}
