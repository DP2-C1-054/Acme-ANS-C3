
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> availableFlights;
		SelectChoices flightChoices;
		SelectChoices travelClassChoices;
		Dataset dataset;
		boolean canPublish;
		Collection<Passenger> passengers;

		availableFlights = this.repository.findPublishedFlights(MomentHelper.getCurrentMoment());
		passengers = this.repository.findPassengersByBookingId(booking.getId());
		flightChoices = SelectChoices.from(availableFlights, "description", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		canPublish = !passengers.isEmpty() && passengers.stream().noneMatch(p -> p.isDraftMode());
		Money price = booking.price();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardNibble", "draftMode");
		dataset.put("price", price);
		dataset.put("canPublish", canPublish);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClasses", travelClassChoices);
		super.getResponse().addData(dataset);
	}

}
