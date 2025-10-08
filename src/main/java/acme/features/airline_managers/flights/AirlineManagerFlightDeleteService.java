
package acme.features.airline_managers.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.legs.Leg;
import acme.entities.passenger.Takes;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status = false;

		if (super.getRequest().hasData("id")) {
			int flightId = super.getRequest().getData("id", int.class);
			Flight flight = this.repository.findFlightById(flightId);

			if (flight != null) {
				AirlineManager manager = flight.getManager();
				status = flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");

	}

	@Override
	public void validate(final Flight flight) {
		boolean isPublished;
		isPublished = true;
		if (this.repository.findLegsByFlightId(flight.getId()).size() > 0) {
			List<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
			for (Leg leg : legs)
				if (!leg.isDraftMode())
					isPublished = leg.isDraftMode();
		}
		super.state(isPublished, "tag", "acme.validation.flight.unable-to-delete-flight-published-leg.message");
	}

	@Override
	public void perform(final Flight flight) {
		List<Booking> bookings;
		List<Leg> legs;
		List<Takes> takes;

		takes = this.repository.findTakesByFlightId(flight.getId());
		bookings = this.repository.findBookingsByFlightId(flight.getId());
		legs = this.repository.findLegsByFlightId(flight.getId());

		this.repository.deleteAll(takes);
		this.repository.deleteAll(bookings);
		this.repository.deleteAll(legs);
		this.repository.delete(flight);
	}

	// Este servicio no tiene unbind, debido a que no se utilizaría en ninguna condición ya que no debes volver a renderizar la página tras eliminar un tramo.
}
