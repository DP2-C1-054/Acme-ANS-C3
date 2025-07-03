
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.realms.assistance_agents.AssistanceAgent;
import acme.realms.assistance_agents.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository repository;


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
			boolean codeContainsInitials = true;

			String code = assistanceAgent.getEmployeeCode();
			String name = assistanceAgent.getUserAccount().getIdentity().getName();
			String surname = assistanceAgent.getUserAccount().getIdentity().getSurname();

			if (!(code == null || name == null || surname == null))
				if (!(StringHelper.isBlank(code) || code.length() < 2 || StringHelper.isBlank(name) || StringHelper.isBlank(surname))) {
					char codeFirstChar = code.toLowerCase().charAt(0);
					char codeSecondChar = code.toLowerCase().charAt(1);
					char nameFirstChar = name.toLowerCase().charAt(0);
					char surnameFirstChar = surname.toLowerCase().charAt(0);
					if (!(codeFirstChar == nameFirstChar && codeSecondChar == surnameFirstChar))
						codeContainsInitials = false;
				}

			super.state(context, codeContainsInitials, "employeeCode", "acme.validation.role.identifier.message");

			List<AssistanceAgent> assistanceAgents = this.repository.findAllAssistanceAgent();
			boolean isUnique = assistanceAgents.stream().noneMatch(a -> a.getEmployeeCode().equals(code) && !a.equals(assistanceAgent));

			if (!isUnique)
				super.state(context, false, "employeeCode", "acme.validation.assistance-agent.code.message");

		}

		result = !super.hasErrors(context);

		return result;
	}

}
