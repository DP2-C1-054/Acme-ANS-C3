
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignments.FlightAssignment;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListPlannedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "duty", "status", "draftMode");
		dataset.put("flightNumber", flightAssignment.getLeg().getFlightNumber());
		dataset.put("scheduledDeparture", flightAssignment.getLeg().getScheduledDeparture());
		super.addPayload(dataset, flightAssignment, "id", "remarks");
		super.getResponse().addData(dataset);

	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		List<FlightAssignment> flightAssignments;

		flightAssignments = this.repository.findPlannedFlightAssignments(flightCrewMember.getId());

		super.getBuffer().addData(flightAssignments);
	}

}
