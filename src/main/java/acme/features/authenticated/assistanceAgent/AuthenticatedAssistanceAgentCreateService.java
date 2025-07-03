
package acme.features.authenticated.assistanceAgent;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.realms.assistance_agents.AssistanceAgent;

@GuiService
public class AuthenticatedAssistanceAgentCreateService extends AbstractGuiService<Authenticated, AssistanceAgent> {

	@Autowired
	private AuthenticatedAssistanceAgentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		String method;
		Airline airline;
		int airlineId;

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = !super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		else {
			airlineId = super.getRequest().getData("airline", int.class);
			airline = this.repository.findAirlineById(airlineId);
			status = (airlineId == 0 || airline != null) && !super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgent assistanceAgent;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		assistanceAgent = new AssistanceAgent();
		assistanceAgent.setUserAccount(userAccount);

		super.getBuffer().addData(assistanceAgent);
	}

	@Override
	public void bind(final AssistanceAgent assistanceAgent) {
		super.bindObject(assistanceAgent, "employeeCode", "spokenLanguages", "moment", "bio", "salary", "photoUrl", "airline");
	}

	@Override
	public void validate(final AssistanceAgent assistanceAgent) {
		;
	}

	@Override
	public void perform(final AssistanceAgent assistanceAgent) {
		this.repository.save(assistanceAgent);
	}

	@Override
	public void unbind(final AssistanceAgent assistanceAgent) {
		Collection<Airline> airlines;
		SelectChoices choices;
		Dataset dataset;

		airlines = this.repository.findAllAirlines();
		choices = SelectChoices.from(airlines, "name", assistanceAgent.getAirline());

		dataset = super.unbindObject(assistanceAgent, "employeeCode", "spokenLanguages", "moment", "bio", "salary", "photoUrl", "airline");
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
