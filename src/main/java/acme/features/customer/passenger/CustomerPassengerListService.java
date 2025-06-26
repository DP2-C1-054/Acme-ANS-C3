
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Passenger> pasenger;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		pasenger = this.repository.findPassengersByCustomerId(customerId);

		super.getBuffer().addData(pasenger);
		super.getResponse().addGlobal("showCreate", true);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "name", "passport");

		super.addPayload(dataset, passenger, "specialNeeds", "mail");

		super.getResponse().addData(dataset);
	}
}
