
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
import acme.entities.legs.Leg;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AirlineManagerLegPublishService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status = false;

		if (super.getRequest().hasData("id")) {
			int legId = super.getRequest().getData("id", int.class);
			int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

			Optional<Leg> optionalLeg = this.repository.findByLegId(legId);

			if (optionalLeg.isPresent()) {
				Leg leg = optionalLeg.get();

				boolean isDraft = leg.isDraftMode();
				boolean managerOwnsFlight = this.repository.findByIdAndManagerId(leg.getFlight().getId(), managerId).isPresent();

				if (isDraft && managerOwnsFlight) {
					status = true;

					if (super.getRequest().hasData("aircraft")) {
						int aircraftId = super.getRequest().getData("aircraft", int.class);
						Aircraft aircraft = this.repository.findAircraftByAircraftId(aircraftId);
						List<Aircraft> aircrafts = this.repository.findAllAircraftsByManagerId(managerId);

						if (aircraftId != 0 && aircraft == null || aircraft != null && !aircrafts.contains(aircraft))
							status = false;
					}

					List<Airport> airports = this.repository.findAllAirports();

					if (super.getRequest().hasData("departureAirport")) {
						int departureId = super.getRequest().getData("departureAirport", int.class);
						Airport departure = this.repository.findAirportByAirportId(departureId);

						if (departureId != 0 && departure == null || departure != null && !airports.contains(departure))
							status = false;
					}

					if (super.getRequest().hasData("arrivalAirport")) {
						int arrivalId = super.getRequest().getData("arrivalAirport", int.class);
						Airport arrival = this.repository.findAirportByAirportId(arrivalId);

						if (arrivalId != 0 && arrival == null || arrival != null && !airports.contains(arrival))
							status = false;
					}
				}
			}
		}

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
	public void bind(final Leg leg) {
		int aircraftId;
		int airportArrivalId;
		int airportDepartureId;
		Aircraft aircraft;
		Airport departure;
		Airport arrival;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftByAircraftId(aircraftId);
		airportDepartureId = super.getRequest().getData("departureAirport", int.class);
		airportArrivalId = super.getRequest().getData("arrivalAirport", int.class);
		departure = this.repository.findAirportByAirportId(airportDepartureId);
		arrival = this.repository.findAirportByAirportId(airportArrivalId);

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
	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
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

		super.getResponse().addData(dataset);
	}

}
