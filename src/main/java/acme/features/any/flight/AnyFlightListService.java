
package acme.features.any.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;

@GuiService
public class AnyFlightListService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag");

		dataset.put("departure", flight.getOriginCity());
		dataset.put("arrival", flight.getDestinationCity());

		super.getResponse().addData(dataset);

	}

	@Override
	public void load() {

		Collection<Flight> flights;

		flights = this.repository.findAllPublishedFlights();

		super.getBuffer().addData(flights);
	}

}
