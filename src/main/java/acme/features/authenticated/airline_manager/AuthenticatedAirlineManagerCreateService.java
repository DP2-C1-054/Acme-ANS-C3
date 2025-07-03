
package acme.features.authenticated.airline_manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AuthenticatedAirlineManagerCreateService extends AbstractGuiService<Authenticated, AirlineManager> {

	@Autowired
	private AuthenticatedAirlineManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AirlineManager object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new AirlineManager();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AirlineManager object) {
		assert object != null;
		super.bindObject(object, "identifierNumber", "experience", "birthdate", "airline", "linkPicture");
	}

	@Override
	public void validate(final AirlineManager object) {
		assert object != null;
	}

	@Override
	public void perform(final AirlineManager object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AirlineManager airlineManager) {
		Dataset dataset;
		SelectChoices airlineChoices;
		List<Airline> airlines;

		airlines = this.repository.findAllAirlines();
		airlineChoices = SelectChoices.from(airlines, "name", airlineManager.getAirline());

		dataset = super.unbindObject(airlineManager, "identifierNumber", "experience", "birthdate", "linkPicture");
		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
