
package acme.features.airline_managers.legs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.legs.Leg;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Leg> managerLegs;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		managerLegs = this.repository.findManagerLegsByManagerIOrderedByMoment(managerId);

		super.getBuffer().addData(managerLegs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture");
		dataset.put("airportDeparture", leg.getDepartureAirport().getName());
		dataset.put("airportArrival", leg.getArrivalAirport().getName());

		super.getResponse().addData(dataset);
	}

}
