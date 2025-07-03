
package acme.features.any.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.legs.Leg;

@GuiService
public class AnyLegListService extends AbstractGuiService<Any, Leg> {

	@Autowired
	private AnyLegRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture");
		dataset.put("airportDeparture", leg.getDepartureAirport().getName());
		dataset.put("airportArrival", leg.getArrivalAirport().getName());
		dataset.put("flight", leg.getFlight());

		super.getResponse().addData(dataset);

	}

	@Override
	public void load() {
		int flightId;

		flightId = super.getRequest().getData("flightId", int.class);
		List<Leg> legs = this.repository.findAllLegsByFlightId(flightId);

		super.getBuffer().addData(legs);
	}

}
