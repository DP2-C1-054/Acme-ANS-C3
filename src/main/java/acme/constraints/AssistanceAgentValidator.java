
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.assistance_agents.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidEmployeeCode, AssistanceAgent> {

	@Override
	protected void initialise(final ValidEmployeeCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assistanceAgent != null) {
			boolean codeContaintsInitials;

			try {
				String code = assistanceAgent.getEmployeeCode();
				String name = assistanceAgent.getUserAccount().getIdentity().getName();
				String surname = assistanceAgent.getUserAccount().getIdentity().getSurname();

				int length = code.length();
				String initials;
				if (length == 8)
					initials = code.substring(0, 2);
				else
					initials = code.substring(0, 3);

				String expectedInitials = (name.substring(0, 1) + surname.substring(0, 1)).toUpperCase();
				String[] surnameParts = surname.split("\\s+");
				if (surnameParts.length > 1)
					expectedInitials += surnameParts[1].substring(0, 1).toUpperCase();

				codeContaintsInitials = initials == expectedInitials;

			} catch (Error e) {
				codeContaintsInitials = false;
			}

			super.state(context, codeContaintsInitials, "*", "acme.validation.employee-code.initials.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
