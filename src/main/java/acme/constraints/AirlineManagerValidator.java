
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.airline_managers.AirlineManager;
import acme.realms.airline_managers.AirlineManagerRepository;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	@Autowired
	private AirlineManagerRepository repository;


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
			boolean codeContainsInitials = true;

			String code = airlineManager.getIdentifierNumber();
			String name = airlineManager.getUserAccount().getIdentity().getName();
			String surname = airlineManager.getUserAccount().getIdentity().getSurname();

			if (code != null && name != null && surname != null)
				codeContainsInitials = code.charAt(0) == name.charAt(0) && code.charAt(1) == surname.charAt(0);
			else
				super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

			List<AirlineManager> airlineManagers = this.repository.findAllAirlineManagers();
			boolean isUnique = airlineManagers.stream().noneMatch(am -> am.getIdentifierNumber().equals(code) && !am.equals(airlineManager));

			if (!isUnique)
				super.state(context, false, "*", "acme.validation.airline-manager.identifier-number.message");

			if (!codeContainsInitials)
				super.state(context, codeContainsInitials, "*", "acme.validation.role.identifier.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
