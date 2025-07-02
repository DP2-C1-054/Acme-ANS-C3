
package acme.features.administrator.pasenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passenger.Passenger;

@GuiController
public class AdministratorPassengerController extends AbstractGuiController<Administrator, Passenger> {

	@Autowired
	private AdministratorPassengerShowService			showService;

	@Autowired
	private AdministratorPassengerBookingListService	bookingListService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);

		super.addCustomCommand("bookingList", "list", this.bookingListService);
	}
}
