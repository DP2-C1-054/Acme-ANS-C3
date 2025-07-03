
package acme.features.authenticated.airline_manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.airline_managers.AirlineManager;

@GuiController
public class AuthenticatedAirlineManagerController extends AbstractGuiController<Authenticated, AirlineManager> {

	@Autowired
	private AuthenticatedAirlineManagerCreateService	createService;

	@Autowired
	private AuthenticatedAirlineManagerUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
