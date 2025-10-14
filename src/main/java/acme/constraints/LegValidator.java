
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Autowired
	private LegRepository repository;


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		List<Leg> legs = this.repository.findAllLegs();

		if (leg.getFlightNumber() != null)
			if (StringHelper.isBlank(leg.getFlightNumber()))
				super.state(context, false, "flightNumber", "javax.validation.constraints.NotBlank.message");
			else if (leg.getAircraft() == null)
				super.state(context, false, "flightNumber", "acme.validation.legs.aircraft.message");
			else if (leg.getAircraft() != null && leg.getFlightNumber() != null) {
				String airlineIataCode = leg.getAircraft().getAirline().getIataCode();
				if (!leg.getFlightNumber().substring(0, 3).equalsIgnoreCase(airlineIataCode))
					super.state(context, false, "flightNumber", "acme.validation.legs.flightNumber.iata.message");
				boolean isUnique = legs.stream().noneMatch(l -> l.getFlightNumber().equals(leg.getFlightNumber()) && !l.equals(leg));
				if (!isUnique)
					super.state(context, false, "flightNumber", "acme.validation.legs.flightNumber.message");
			}

		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null && leg.getScheduledDeparture().compareTo(leg.getScheduledArrival()) >= 0)
			super.state(context, false, "scheduledDeparture", "acme.validation.legs.scheduledArrivalDeparture.message");

		if (leg.getArrivalAirport() != null && leg.getArrivalAirport() != null && leg.getDepartureAirport().equals(leg.getArrivalAirport()))
			super.state(context, false, "departureAirport", "acme.validation.legs.departureArrivalAirport.message");

		if (leg.getFlight() != null) {
			boolean isLegOverlapping = this.repository.isLegOverlapping(leg.getId(), leg.getFlight().getId(), leg.getScheduledDeparture(), leg.getScheduledArrival());
			if (isLegOverlapping)
				super.state(context, false, "scheduledDeparture", "acme.validation.leg.overlapping-legs.message");
		}

		return !super.hasErrors(context);
	}

}
