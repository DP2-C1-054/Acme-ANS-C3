
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
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
			String iataCode = aircraft.getRegistrationNumber();

			if (iataCode == null)
				super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

			List<Aircraft> aircrafts = this.repository.findAllAircrafts();
			boolean isUnique = aircrafts.stream().filter(a -> a.getRegistrationNumber().equals(iataCode)).count() == 1;

			if (!isUnique)
				super.state(context, false, "*", "acme.validation.aircraft.registration-number.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
