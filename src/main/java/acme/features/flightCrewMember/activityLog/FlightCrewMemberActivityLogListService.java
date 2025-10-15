
package acme.features.flightCrewMember.activityLog;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_logs.ActivityLog;
import acme.entities.flight_assignments.FlightAssignment;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		boolean status = true;
		Integer flightAssignmentId = 0;
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().hasData("id"))
			flightAssignmentId = super.getRequest().getData("id", int.class);

		if (this.repository.findFlightAssignmentById(flightAssignmentId).get().getAllocatedFlightCrewMember().getId() != userId)
			status = false;

		if (this.repository.findFlightAssignmentById(flightAssignmentId).get().isDraftMode())
			status = false;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "severityLevel", "incidentDescription", "flightAssignment", "draftMode");

		FlightAssignment flightAssignment = this.repository.findFlightAssignmentByActivityLogId(activityLog.getId());

		if (flightAssignment != null)
			dataset.put("moment", flightAssignment.getMoment());

		super.getResponse().addData(dataset);
	}

	@Override
	public void load() {

		int flightAssignmentId = 0;
		Collection<ActivityLog> activityLogs = new ArrayList<>();
		if (super.getRequest().hasData("id")) {
			flightAssignmentId = super.getRequest().getData("id", int.class);
			super.getResponse().addGlobal("id", flightAssignmentId);
		}

		if (flightAssignmentId != 0)
			activityLogs = this.repository.findActivityLogsByFlightAssignmentId(flightAssignmentId);

		super.getBuffer().addData(activityLogs);
	}

}
