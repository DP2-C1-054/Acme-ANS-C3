
package acme.features.airline_managers.dashboards;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.airline_manager.AirlineManagerDashboard;
import acme.realms.airline_managers.AirlineManager;

@GuiController
public class AirlineManagerDashboardController extends AbstractGuiController<AirlineManager, AirlineManagerDashboard> {

	@Autowired
	private AirlineManagerDashboardShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
