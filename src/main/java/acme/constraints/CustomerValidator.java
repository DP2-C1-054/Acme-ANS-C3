
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.realms.customer.Customer;
import acme.realms.customer.CustomerRepository;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private CustomerRepository repository;


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

			String code = customer.getIdentifier();
			String name = customer.getUserAccount().getIdentity().getName();
			String surname = customer.getUserAccount().getIdentity().getSurname();
			if (!StringHelper.isBlank(code) && !StringHelper.isBlank(name) && !StringHelper.isBlank(surname) && code.length() >= 2) {
				char codeFirstChar = code.charAt(0);
				char codeSecondChar = code.charAt(1);
				char nameFirstChar = name.charAt(0);
				char surnameFirstChar = surname.charAt(0);

				if (!(codeFirstChar == nameFirstChar && codeSecondChar == surnameFirstChar))
					codeContainsInitials = false;

				super.state(context, codeContainsInitials, "*", "acme.validation.customer.identifier.message");

				List<Customer> customers = this.repository.findAllCustomers();
				boolean isUnique = customers.stream().noneMatch(c -> c.getIdentifier().equals(code) && !c.equals(customer));

				if (!isUnique)
					super.state(context, false, "*", "acme.validation.customer.uniqueIdentifier.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
