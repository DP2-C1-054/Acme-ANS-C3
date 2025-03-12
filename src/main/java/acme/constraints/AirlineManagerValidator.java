
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline_managers.AirlineManager;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	@Override
	protected void initialise(final ValidAirlineManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (airlineManager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean codeContainsInitials;

			try {
				String code = airlineManager.getIdentifierNumber();
				String name = airlineManager.getUserAccount().getIdentity().getName();
				String surname = airlineManager.getUserAccount().getIdentity().getSurname();

				String initials = code.substring(0, 2);

				String expectedInitials = (name.substring(0, 1) + surname.substring(0, 1)).toUpperCase();

				codeContainsInitials = initials == expectedInitials;

			} catch (Error e) {
				codeContainsInitials = false;
			}

			super.state(context, codeContainsInitials, "*", "acme.validation.role.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
