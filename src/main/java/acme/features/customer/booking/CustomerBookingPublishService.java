
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
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean canPublish;
		int flightId;
		Flight flight;
		String travelClass;
		List<String> travelClasses;
		int masterId;
		Booking booking;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(masterId);
		customer = booking == null ? null : booking.getCustomer();
		Collection<Passenger> passengers = this.repository.findPassengersByBookingId(masterId);
		boolean allPublished = passengers.stream().allMatch(p -> !p.isDraftMode());
		canPublish = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer) && !passengers.isEmpty() && allPublished;

		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = canPublish;
		else {
			Date currentMoment;
			currentMoment = MomentHelper.getCurrentMoment();
			travelClass = super.getRequest().getData("travelClass", String.class);
			travelClasses = List.of(TravelClass.values()).stream().map(t -> t.name()).toList();
			flightId = super.getRequest().getData("flight", int.class);
			flight = this.repository.findFlightById(flightId);
			status = canPublish && (travelClasses.contains(travelClass) || StringHelper.isEqual(travelClass, "0", false))
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
		boolean isValid = booking.getCreditCardNibble() != null;
		super.state(isValid, "creditCardNibble", "acme.validation.booking.draftModeWithouNibble.message");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> availableFlights;
		SelectChoices flightChoices;
		SelectChoices travelClassChoices;
		Dataset dataset;
		Date currentMoment;
		boolean canPublish;
		Collection<Passenger> passengers;
		Collection<Flight> publishedFlights;

		availableFlights = this.repository.findPublishedFlights(MomentHelper.getCurrentMoment());
		flightChoices = SelectChoices.from(availableFlights, "description", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		passengers = this.repository.findPassengersByBookingId(booking.getId());
		canPublish = !passengers.isEmpty() && passengers.stream().noneMatch(p -> p.isDraftMode());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardNibble", "draftMode");
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		dataset.put("canPublish", canPublish);
		dataset.put("flights", flightChoices);
		dataset.put("travelClasses", travelClassChoices);

		super.getResponse().addData(dataset);
	}
}
