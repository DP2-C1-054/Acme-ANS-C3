
package acme.features.airline_managers.legs;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
		boolean authorised = false;

		if (super.getRequest().hasData("flightId", int.class)) {
			int flightId = super.getRequest().getData("flightId", int.class);
			Flight flight = this.flightRepository.findFlightById(flightId);

			if (flight != null && flight.isDraftMode()) {
				int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
				Optional<Flight> optionalFlight = this.repository.findByIdAndManagerId(flightId, managerId);

				if (optionalFlight.isPresent()) {
					authorised = true;

					if (super.getRequest().hasData("id", boolean.class)) {
						int legId = super.getRequest().getData("id", int.class);
						if (legId != 0)
							authorised = false;
					}

					if (authorised && "POST".equals(super.getRequest().getMethod())) {
						int aircraftId = super.getRequest().getData("aircraft", int.class);
						int departureId = super.getRequest().getData("departureAirport", int.class);
						int arrivalId = super.getRequest().getData("arrivalAirport", int.class);

						Aircraft aircraft = this.repository.findAircraftByAircraftId(aircraftId);
						Airport departure = this.repository.findAirportByAirportId(departureId);
						Airport arrival = this.repository.findAirportByAirportId(arrivalId);

						List<Aircraft> managerAircrafts = this.repository.findAllAircraftsByManagerId(managerId);
						List<Airport> allAirports = this.repository.findAllAirports();

						if (aircraftId != 0 && (aircraft == null || !managerAircrafts.contains(aircraft)) || departureId != 0 && (departure == null || !allAirports.contains(departure))
							|| arrivalId != 0 && (arrival == null || !allAirports.contains(arrival)))
							authorised = false;
					}
				}
			}
		}

		super.getResponse().setAuthorised(authorised);
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
		Date currentDate = MomentHelper.getBaseMoment();

		if (leg.getScheduledDeparture() != null) {
			boolean isValidScheduledDeparture = MomentHelper.isBefore(currentDate, leg.getScheduledDeparture());

			if (!isValidScheduledDeparture)
				super.state(false, "scheduledDeparture", "acme.validation.legs.scheduledDeparture.message");
		}

		if (leg.getScheduledArrival() != null) {
			boolean isValidScheduledArrival = MomentHelper.isBefore(currentDate, leg.getScheduledArrival());

			if (!isValidScheduledArrival)
				super.state(false, "scheduledArrival", "acme.validation.legs.scheduledArrival.message");
		}

		if (leg.getDepartureAirport() != null && leg.getScheduledArrival() != null && leg.getScheduledDeparture().compareTo(leg.getScheduledArrival()) >= 0)
			super.state(false, "scheduledDeparture", "acme.validation.legs.scheduledArrivalDeparture.message");

	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		SelectChoices departureChoices;
		SelectChoices arrivalChoices;
		Dataset dataset;
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
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("departureAirport", departureChoices.getSelected().getKey());
		dataset.put("departureAirports", departureChoices);
		dataset.put("arrivalAirport", arrivalChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalChoices);
		dataset.put("flight", leg.getFlight());
		dataset.put("flightId", leg.getFlight().getId());

		super.getResponse().addData(dataset);
	}

}
