
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineRepository;

@Validator
public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	@Autowired
	private AirlineRepository repository;


	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}
	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (airline == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		else {
			String iataCode = airline.getIataCode();

			if (iataCode != null && !StringHelper.isBlank(iataCode)) {
				List<Airline> airlines = this.repository.findAllAirline();
				boolean isUnique = airlines.stream().noneMatch(a -> a.getIataCode().equals(iataCode) && !a.equals(airline));

				if (!isUnique)
					super.state(context, false, "iataCode", "acme.validation.airline.iata-code.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
