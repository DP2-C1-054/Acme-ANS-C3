
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.passenger.Passenger;

public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	@Override
	protected void initialise(final ValidPassenger annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (passenger == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String passport = passenger.getPassport();
			if (passport == null)
				super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		}
		result = !super.hasErrors(context);

		return result;
	}

}
