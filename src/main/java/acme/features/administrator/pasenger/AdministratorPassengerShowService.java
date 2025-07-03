
package acme.features.administrator.pasenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.Takes;

@GuiService
public class AdministratorPassengerShowService extends AbstractGuiService<Administrator, Passenger> {

	@Autowired
	private AdministratorPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int passengerId;
		Passenger passenger;
		Collection<Takes> takesOfPassengerWithPublishedBooking;

		passengerId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(passengerId);
		takesOfPassengerWithPublishedBooking = this.repository.findTakesOfPassengerWithPublishedBooking(passengerId);

		status = !passenger.isDraftMode() && !takesOfPassengerWithPublishedBooking.isEmpty();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "name", "mail", "passport", "birthDate", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}
}
