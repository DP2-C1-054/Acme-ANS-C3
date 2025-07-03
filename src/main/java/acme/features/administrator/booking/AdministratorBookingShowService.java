
package acme.features.administrator.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = !booking.isDraftMode();

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

		availableFlights = List.of(booking.getFlight());
		flightChoices = SelectChoices.from(availableFlights, "description", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Money price = booking.price();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardNibble", "draftMode");
		dataset.put("price", price);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClasses", travelClassChoices);
		super.getResponse().addData(dataset);
	}

}
