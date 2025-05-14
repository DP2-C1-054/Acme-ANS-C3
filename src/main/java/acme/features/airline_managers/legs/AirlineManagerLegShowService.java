
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
public class AirlineManagerLegShowService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository		repository;

	@Autowired
	private AirlineManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		AirlineManager manager;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByLegId(legId);
		manager = leg == null ? null : leg.getFlight().getManager();
		status = super.getRequest().getPrincipal().hasRealm(manager) && leg != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByLegId(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices statusChoices;
		SelectChoices flightsChoices;
		SelectChoices aircraftChoices;
		SelectChoices departureChoices;
		SelectChoices arrivalChoices;
		Dataset dataset;
		List<Flight> flights;
		List<Aircraft> aircrafts;
		List<Airport> airports;
		int managerId;

		statusChoices = SelectChoices.from(Leg.Status.class, leg.getStatus());

		if (!leg.isDraftMode()) {
			flights = this.flightRepository.findAllFlights();
			aircrafts = this.repository.findAllAircrafts();
		} else {
			managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			flights = this.flightRepository.findManagerFlightsByManagerId(managerId);
			aircrafts = this.repository.findAllAircraftsByManagerId(managerId);
		}

		flightsChoices = SelectChoices.from(flights, "tag", leg.getFlight());
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());

		airports = this.repository.findAllAirports();
		departureChoices = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		arrivalChoices = SelectChoices.from(airports, "name", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		dataset.put("duration", leg.durationInHours());
		dataset.put("statuses", statusChoices);
		dataset.put("flight", flightsChoices.getSelected().getKey());
		dataset.put("flights", flightsChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("departureAirport", departureChoices.getSelected().getKey());
		dataset.put("departureAirports", departureChoices);
		dataset.put("arrivalAirport", arrivalChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalChoices);

		super.getResponse().addData(dataset);
	}

}
