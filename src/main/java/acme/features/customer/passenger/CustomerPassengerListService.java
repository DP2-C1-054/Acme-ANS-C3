
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> pasenger;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		pasenger = this.repository.findPassengersByBookingId(bookingId);

		super.getBuffer().addData(pasenger);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "name", "passport");

		super.addPayload(dataset, passenger, "specialNeeds", "mail");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Passenger> passengers) {
		int bookingId;
		Booking booking;
		final boolean showCreate;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		showCreate = booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());
		super.getResponse().addGlobal("bookingId", bookingId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
