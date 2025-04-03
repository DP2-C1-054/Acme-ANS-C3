
package acme.features.airline_managers.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Flight> managerFlights;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		managerFlights = this.repository.findManagerFlightsByManagerId(managerId);

		super.getBuffer().addData(managerFlights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag");

		dataset.put("departure", flight.getOriginCity());
		dataset.put("arrival", flight.getDestinationCity());

		super.getResponse().addData(dataset);
	}
}
