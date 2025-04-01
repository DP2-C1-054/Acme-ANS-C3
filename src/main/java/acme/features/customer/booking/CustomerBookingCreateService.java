
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setDraftMode(true);
		booking.setCustomer(customer);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardNibble");
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
		int bookingId;

		availableFlights = this.repository.findAvailableFlights();
		flightChoices = SelectChoices.from(availableFlights, "tag", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardNibble", "draftMode");
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClasses", travelClassChoices);
		super.getResponse().addData(dataset);
	}

}
