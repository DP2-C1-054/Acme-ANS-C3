
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.passenger.Takes;
import acme.entities.passenger.TakesRepository;

@Validator
public class TakesValidator extends AbstractValidator<ValidTakes, Takes> {

	@Autowired
	private TakesRepository repository;


	@Override
	protected void initialise(final ValidTakes annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Takes takes, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (takes == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (takes.getPassenger() == null || takes.getBooking() == null)
			super.state(context, false, "passenger", "javax.validation.constraints.NotNull.message");
		else {
			List<Takes> takeses = this.repository.findAllTakes();
			boolean alreadyExist = takeses.stream().anyMatch(t -> !t.equals(takes) && t.getBooking().equals(takes.getBooking()) && t.getPassenger().equals(takes.getPassenger()));

			super.state(context, !alreadyExist, "passenger", "acme.validation.takes.duplicated");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
