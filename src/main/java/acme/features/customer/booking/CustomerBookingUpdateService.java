
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		String travelClass;
		List<String> travelClasses;

		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			Date currentMoment;
			currentMoment = MomentHelper.getCurrentMoment();
			travelClass = super.getRequest().getData("travelClass", String.class);
			travelClasses = List.of(TravelClass.values()).stream().map(t -> t.name()).toList();
			flightId = super.getRequest().getData("flight", int.class);
			flight = this.repository.findFlightById(flightId);
			status = (travelClasses.contains(travelClass) || StringHelper.isEqual(travelClass, "0", false))
				&& (flightId == 0 || flight != null && !flight.isDraftMode() && flight.getScheduledDeparture() != null && MomentHelper.isAfter(flight.getScheduledDeparture(), currentMoment));
		}
		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "creditCardNibble");
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> availableFlights;
		SelectChoices flightChoices;
		SelectChoices travelClassChoices;
		Dataset dataset;
		Date currentMoment;
		Collection<Flight> publishedFlights;

		publishedFlights = this.repository.findPublishedFlights();
		currentMoment = MomentHelper.getCurrentMoment();
		availableFlights = publishedFlights.stream().filter(f -> f.getScheduledDeparture() != null && MomentHelper.isAfter(f.getScheduledDeparture(), currentMoment)).toList();
		flightChoices = SelectChoices.from(availableFlights, "description", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardNibble", "draftMode");
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClasses", travelClassChoices);

		super.getResponse().addData(dataset);
	}

}
