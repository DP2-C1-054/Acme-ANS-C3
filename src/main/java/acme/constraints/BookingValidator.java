
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRepository;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private BookingRepository repository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (booking == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String locatorCode = booking.getLocatorCode();
			if (locatorCode == null)
				super.state(context, false, "locatorCode", "javax.validation.constraints.NotNull.message");
			else {
				List<Booking> bookings = this.repository.findAllBookings();
				boolean isUnique = bookings.stream().noneMatch(b -> b.getLocatorCode().equals(locatorCode) && !b.equals(booking));
				if (!isUnique)
					super.state(context, false, "locatorCode", "acme.validation.booking.uniqueLocatorCode.message");

			}
			boolean draftMode = booking.isDraftMode();
			if (!draftMode) {
				if (booking.getCreditCardNibble() == null)
					super.state(context, false, "creditCardNibble", "acme.validation.booking.draftModeWithouNibble.message");
				List<Passenger> passengers = this.repository.findAllPassengerByBookingId(booking.getId());
				if (passengers.isEmpty())
					super.state(context, false, "*", "acme.validation.booking.draftModeWithouPassenger.message");
				if (passengers.stream().anyMatch(p -> p.isDraftMode()))
					super.state(context, false, "*", "acme.validation.booking.draftModeWithNotPublishedPassenger.message");

			}
			Flight flight = booking.getFlight();

			if (flight != null && (flight.getScheduledDeparture() != null && !MomentHelper.isAfter(flight.getScheduledDeparture(), MomentHelper.getCurrentMoment()) || flight.isDraftMode()))
				super.state(context, false, "flight", "acme.validation.booking.flight.notValid.message");

		}
		result = !super.hasErrors(context);

		return result;
	}

}
