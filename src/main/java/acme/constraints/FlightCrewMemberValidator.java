
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.flight_crew_members.FlightCrewMember;

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
			boolean codeContaintsInitials;

			try {
				String code = flightCrewMember.getEmployeeCode();
				String name = flightCrewMember.getUserAccount().getIdentity().getName();
				String surname = flightCrewMember.getUserAccount().getIdentity().getSurname();

				String initials = code.substring(0, 2);
				;
				String expectedInitials = (name.substring(0, 1) + surname.substring(0, 1)).toUpperCase();

				codeContaintsInitials = initials == expectedInitials;

			} catch (Error e) {
				codeContaintsInitials = false;
			}

			super.state(context, codeContaintsInitials, "*", "acme.validation.role.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
