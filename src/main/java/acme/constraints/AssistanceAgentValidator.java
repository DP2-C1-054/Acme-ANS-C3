
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.assistance_agents.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean codeContaintsInitials;

			try {
				String code = assistanceAgent.getEmployeeCode();
				String name = assistanceAgent.getUserAccount().getIdentity().getName();
				String surname = assistanceAgent.getUserAccount().getIdentity().getSurname();

				String initials = code.substring(0, 2);

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
