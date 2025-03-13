
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.customers.Customer;

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
			boolean codeContaintsInitials;

			try {
				String code = customer.getIdentifier();
				String name = customer.getUserAccount().getIdentity().getName();
				String surname = customer.getUserAccount().getIdentity().getSurname();

				String initials = code.substring(0, 2);

				String expectedInitials = (name.substring(0, 1) + surname.substring(0, 1)).toUpperCase();
				codeContaintsInitials = initials == expectedInitials;

			} catch (Error e) {
				codeContaintsInitials = false;
			}

			super.state(context, codeContaintsInitials, "*", "acme.validation.customer.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
