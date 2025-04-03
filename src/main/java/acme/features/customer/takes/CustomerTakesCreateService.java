
package acme.features.customer.takes;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.Takes;
import acme.realms.customer.Customer;

@GuiService
public class CustomerTakesCreateService extends AbstractGuiService<Customer, Takes> {

	@Autowired
	private CustomerTakesRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		Takes takes;
		int bookingId;
		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		takes = new Takes();
		takes.setBooking(booking);
		super.getBuffer().addData(takes);

	}

	@Override
	public void bind(final Takes takes) {
		int pasengerId;
		Passenger passenger;

		pasengerId = super.getRequest().getData("passenger", int.class);
		passenger = this.repository.findPassengerById(pasengerId);

		super.bindObject(takes);
		takes.setPassenger(passenger);
	}

	@Override
	public void validate(final Takes takes) {
		;
	}

	@Override
	public void perform(final Takes takes) {
		this.repository.save(takes);
	}

	@Override
	public void unbind(final Takes takes) {
		Collection<Passenger> availablePassengers;
		SelectChoices passengerChoices;
		Dataset dataset;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		availablePassengers = this.repository.findAvailablePassengers(bookingId);
		passengerChoices = SelectChoices.from(availablePassengers, "passport", takes.getPassenger());
		dataset = super.unbindObject(takes);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);
		bookingId = super.getRequest().getData("bookingId", int.class);
		dataset.put("bookingId", bookingId);
		super.getResponse().addData(dataset);
	}
}
