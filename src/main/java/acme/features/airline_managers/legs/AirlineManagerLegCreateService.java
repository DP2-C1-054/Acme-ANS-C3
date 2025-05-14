
package acme.features.airline_managers.legs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.legs.Leg;
import acme.features.airline_managers.flights.AirlineManagerFlightRepository;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository		repository;

	@Autowired
	private AirlineManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Leg leg;
		Flight flight;

		Integer flightId = super.getRequest().getData("flightId", int.class);
		flight = this.flightRepository.findFlightById(flightId);

		leg = new Leg();
		leg.setFlight(flight);
		leg.setDraftMode(true);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int aircraftId;
		int departureId;
		int arrivalId;
		Aircraft aircraft;
		Airport departure;
		Airport arrival;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftByAircraftId(aircraftId);
		departureId = super.getRequest().getData("departureAirport", int.class);
		departure = this.repository.findAirportByAirportId(departureId);
		arrivalId = super.getRequest().getData("arrivalAirport", int.class);
		arrival = this.repository.findAirportByAirportId(arrivalId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departure);
		leg.setArrivalAirport(arrival);

	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		SelectChoices departureChoices;
		SelectChoices arrivalChoices;
		List<Aircraft> aircrafts;
		List<Airport> airports;
		int managerId;

		statusChoices = SelectChoices.from(Leg.Status.class, leg.getStatus());
		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		aircrafts = this.repository.findAllAircraftsByManagerId(managerId);
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		airports = this.repository.findAllAirports();
		departureChoices = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		arrivalChoices = SelectChoices.from(airports, "name", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");

		dataset.put("statuses", statusChoices);
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("departureAirports", departureChoices);
		dataset.put("arrivalAirports", arrivalChoices);

		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("departureAirport", departureChoices.getSelected().getKey());
		dataset.put("arrivalAirport", arrivalChoices.getSelected().getKey());

		dataset.put("flight", leg.getFlight());
		dataset.put("flightId", leg.getFlight().getId());

		super.getResponse().addData(dataset);
	}

}
