
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
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
			boolean correctCode;

			try {

				String airlineIataCode = leg.getAircraft().getAirline().getIataCode();
				correctCode = leg.getFlightNumber().substring(0, 3).toUpperCase().equals(airlineIataCode.toUpperCase());

			} catch (Error e) {
				correctCode = false;
			}

			super.state(context, correctCode, "*", "acme.validation.legs.flight-number.message");
		}
		result = !super.hasErrors(context);

		return result;
	}
}
