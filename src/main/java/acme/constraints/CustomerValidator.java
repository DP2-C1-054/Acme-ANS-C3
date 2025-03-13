
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.customer.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean codeContainsInitials = true;

			try {
				String code = customer.getIdentifier();
				String name = customer.getUserAccount().getIdentity().getName();
				String surname = customer.getUserAccount().getIdentity().getSurname();

				char codeFirstChar = Character.toUpperCase(code.charAt(0));
				char codeSecondChar = Character.toUpperCase(code.charAt(1));
				char nameFirstChar = Character.toUpperCase(name.charAt(0));
				char surnameFirstChar = Character.toUpperCase(surname.charAt(0));

				if (!(codeFirstChar == nameFirstChar && codeSecondChar == surnameFirstChar))
					codeContainsInitials = false;

			} catch (Error e) {
				codeContainsInitials = false;
			}

			super.state(context, codeContainsInitials, "*", "acme.validation.customer.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
