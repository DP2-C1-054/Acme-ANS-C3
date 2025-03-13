
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.legs.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correct;

			try {

				String airlineIataCode = leg.getAircraft().getAirline().getIataCode();
				Date currentDate = MomentHelper.getCurrentMoment();

				boolean correctCode = leg.getFlightNumber().substring(0, 3).equalsIgnoreCase(airlineIataCode);
				boolean correctDepartureArrivalDates = leg.getScheduledDeparture().compareTo(leg.getScheduledArrival()) < 0;
				boolean correctDate = leg.getScheduledDeparture().compareTo(currentDate) > 0 && leg.getScheduledArrival().compareTo(currentDate) > 0;

				correct = correctCode && correctDepartureArrivalDates && correctDate;

				if (!correctCode)
					super.state(context, correct, "*", "acme.validation.legs.flight-number.message");
				if (!correctDate)
					super.state(context, correct, "*", "acme.validation.legs.current-dates.message");
				if (!correctDepartureArrivalDates)
					super.state(context, correct, "*", "acme.validation.legs.departure-arrival-date.message");

			} catch (Exception e) {
				correct = false;
			}

		}
		result = !super.hasErrors(context);

		return result;
	}
}
