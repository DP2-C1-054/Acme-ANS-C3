
package acme.features.administrator.pasenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;

@GuiService
public class AdministratorPassengerBookingListService extends AbstractGuiService<Administrator, Passenger> {

	@Autowired
	private AdministratorPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = booking != null && !booking.isDraftMode();

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

		bookingId = super.getRequest().getData("bookingId", int.class);
		super.getResponse().addGlobal("bookingId", bookingId);
	}
}
