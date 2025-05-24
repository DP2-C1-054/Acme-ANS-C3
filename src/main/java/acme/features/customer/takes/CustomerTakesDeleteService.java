
package acme.features.customer.takes;

import java.util.Collection;
import java.util.List;

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
public class CustomerTakesDeleteService extends AbstractGuiService<Customer, Takes> {

	@Autowired
	private CustomerTakesRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;
		String method = super.getRequest().getMethod();
		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		int passengerId;
		Passenger passenger;

		if (method.equals("GET")) {
			List<Takes> takes = this.repository.findTakesByBookingId(bookingId);
			status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer) && !takes.isEmpty();
		} else {
			passengerId = super.getRequest().getData("passenger", int.class);
			passenger = this.repository.findPassengerById(passengerId);
			List<Takes> takes = this.repository.findTakesByBookingId(bookingId);
			Customer passengerCustomer = passenger == null ? null : passenger.getCustomer();
			Boolean hasAlreadyExist = takes.stream().anyMatch(t -> t.getPassenger().equals(passenger));
			status = passenger != null && booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer) && super.getRequest().getPrincipal().hasRealm(passengerCustomer) && hasAlreadyExist;
		}
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
		int passengerId;
		Passenger passenger;
		int bookingId = super.getRequest().getData("bookingId", int.class);
		passengerId = super.getRequest().getData("passenger", int.class);
		passenger = this.repository.findPassengerById(passengerId);

		super.bindObject(takes);
		Takes take = this.repository.findTakesByBookingAndPassengerId(bookingId, passengerId);
		super.getBuffer().addData(take);
		takes.setPassenger(passenger);
	}

	@Override
	public void validate(final Takes takes) {
		;
	}

	@Override
	public void perform(final Takes takes) {
		this.repository.delete(takes);
	}

	@Override
	public void unbind(final Takes takes) {
		Collection<Passenger> availablePassengers;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		SelectChoices passengerChoices;
		Dataset dataset;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		availablePassengers = this.repository.findAlreadyTakesPassengers(bookingId, customer.getId());
		passengerChoices = SelectChoices.from(availablePassengers, "passport", takes.getPassenger());
		dataset = super.unbindObject(takes);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);
		bookingId = super.getRequest().getData("bookingId", int.class);
		dataset.put("bookingId", bookingId);
		super.getResponse().addData(dataset);
	}
}
