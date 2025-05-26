
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftRepository;

@Validator
public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	protected void initialise(final ValidAircraft annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (aircraft == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			String registrationNumber = aircraft.getRegistrationNumber();
			boolean isBlank = StringHelper.isBlank(registrationNumber);
			super.state(context, !isBlank, "registrationNumber", "javax.validation.constraints.NotBlank.message");

			if (!isBlank) {
				List<Aircraft> aircrafts = this.repository.findAllByRegistrationNumber(aircraft.getId(), registrationNumber);
				boolean isUnique = aircrafts.isEmpty();
				super.state(context, isUnique, "registrationNumber", "acme.validation.aircraft.registration-number");
			}

		}

		result = !super.hasErrors(context);
		return result;
	}

}
