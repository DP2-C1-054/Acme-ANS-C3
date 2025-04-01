
package acme.features.airline_managers.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.legs.Leg;
import acme.realms.airline_managers.AirlineManager;

@GuiController
public class AirlineManagerLegController extends AbstractGuiController<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
