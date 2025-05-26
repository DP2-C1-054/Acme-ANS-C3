
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_logs.ActivityLog;
import acme.entities.flight_assignments.FlightAssignment;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);

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
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "incidentDescription", "severityLevel", "flightAssignment", "draftMode");

		Collection<FlightAssignment> flightAssignments = this.repository.findFlightAssignmentsByCrewMemberId(activityLog.getFlightAssignment().getAllocatedFlightCrewMember().getId());
		FlightAssignment currentAssignment = activityLog.getFlightAssignment();
		if (currentAssignment != null && !flightAssignments.contains(currentAssignment))
			flightAssignments.add(currentAssignment);

		SelectChoices assignmentChoices = SelectChoices.from(flightAssignments, "id", activityLog.getFlightAssignment());
		dataset.put("assignments", assignmentChoices);

		super.getResponse().addData(dataset);
	}

}
