
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.PassengerRepository;

public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	@Autowired
	private PassengerRepository repository;


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
			Integer customerId = passenger.getCustomer().getId();
			List<Passenger> samePassport = this.repository.findCustomerPassengerWithPassport(passport, customerId);
			if (passport == null)
				super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			else if (!(samePassport.isEmpty() || samePassport.contains(passenger) && samePassport.size() == 1))
				super.state(context, false, "passport", "acme.validation.passenger.duplicatePassport.message");
		}
		result = !super.hasErrors(context);

		return result;
	}

}
