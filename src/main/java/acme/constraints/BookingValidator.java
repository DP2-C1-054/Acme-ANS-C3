
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRepository;

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
				super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			else {
				List<Booking> bookings = this.repository.findAllBookings();
				boolean isUnique = bookings.stream().noneMatch(b -> b.getLocatorCode().equals(locatorCode) && !b.equals(booking));
				if (!isUnique)
					super.state(context, false, "*", "acme.validation.booking.uniqueLocatorCode.message");
			}
			boolean draftMode = booking.isDraftMode();
			if (!draftMode) {
				if (booking.getCreditCardNibble() == null)
					super.state(context, false, "*", "acme.validation.booking.draftModeWithouNibble.message");
				if (this.repository.findAllPassengerByBookingId(booking.getId()).isEmpty())
					super.state(context, false, "*", "acme.validation.booking.draftModeWithouPassenger.message");
			}

		}
		result = !super.hasErrors(context);

		return result;
	}

}
