
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.flight_crew_members.FlightCrewMember;

public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flightCrewMember == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean codeContainsInitials = true;

			try {
				String code = flightCrewMember.getEmployeeCode();
				String name = flightCrewMember.getUserAccount().getIdentity().getName();
				String surname = flightCrewMember.getUserAccount().getIdentity().getSurname();

				char codeFirstChar = Character.toUpperCase(code.charAt(0));
				char codeSecondChar = Character.toUpperCase(code.charAt(1));
				char nameFirstChar = Character.toUpperCase(name.charAt(0));
				char surnameFirstChar = Character.toUpperCase(surname.charAt(0));

				if (!(codeFirstChar == nameFirstChar && codeSecondChar == surnameFirstChar))
					codeContainsInitials = false;

			} catch (Error e) {
				codeContainsInitials = false;
			}

			super.state(context, codeContainsInitials, "*", "acme.validation.role.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
