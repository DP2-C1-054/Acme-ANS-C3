
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAdministrator = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		String method = super.getRequest().getMethod();
		boolean status;

		if (method.equals("GET"))
			status = true;
		else {
			int airlineId = super.getRequest().getData("airline", int.class);
			Airline airline = this.repository.findAirlineById(airlineId);
			status = airlineId == 0 || airline != null;

		}
		super.getResponse().setAuthorised(status && isAdministrator);
	}

	@Override
	public void load() {
		Aircraft aircraft;

		aircraft = new Aircraft();
		aircraft.setModel("");
		aircraft.setRegistrationNumber("");
		aircraft.setCapacity(1);
		aircraft.setCargoWeight(2000);
		aircraft.setAircraftStatus(null);
		aircraft.setAircraftDetails("");

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "aircraftStatus", "aircraftDetails");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		Aircraft a = this.repository.findAircraftByRegistrationNumber(aircraft.getRegistrationNumber());
		if (a != null)
			super.state(false, "registrationNumber", "acme.validation.confirmation.message.aircraft.registrationNumber");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choices;
		SelectChoices airlineChoices;
		Dataset dataset;

		Collection<Airline> airlines = this.repository.findAllAirlines();
		airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());
		choices = SelectChoices.from(AircraftStatus.class, aircraft.getAircraftStatus());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "aircraftStatus", "aircraftDetails");
		dataset.put("statuses", choices);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
