
package acme.features.any.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignments.FlightAssignment;

@GuiService
public class AnyFlightAssignmentListService extends AbstractGuiService<Any, FlightAssignment> {

	@Autowired
	private AnyFlightAssignmentRepository repository;


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
		super.addPayload(dataset, flightAssignment, "remarks");
		super.getResponse().addData(dataset);

	}

	@Override
	public void load() {

		Collection<FlightAssignment> flightAssignments;

		flightAssignments = this.repository.findAllPublishedFA();

		super.getBuffer().addData(flightAssignments);
	}

}
