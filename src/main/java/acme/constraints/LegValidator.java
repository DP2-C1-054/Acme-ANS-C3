
package acme.constraints;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.aircrafts.Aircraft;
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

		boolean result = false;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correct;

			String flightNumber = leg.getFlightNumber();
			Aircraft aircraft = leg.getAircraft();
			Date scheduledDeparture = leg.getScheduledDeparture();
			Date scheduledArrival = leg.getScheduledArrival();

			if (!StringHelper.isBlank(flightNumber) && flightNumber != null && aircraft != null && scheduledDeparture != null && scheduledArrival != null) {

				String airlineIataCode = leg.getAircraft().getAirline().getIataCode();

				if (StringHelper.isBlank(airlineIataCode) || StringHelper.isBlank(leg.getFlightNumber()) || leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null)
					super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

				boolean correctCode = leg.getFlightNumber().substring(0, 3).equalsIgnoreCase(airlineIataCode);
				boolean correctDepartureArrivalDates = leg.getScheduledDeparture().compareTo(leg.getScheduledArrival()) < 0;

				correct = correctCode && correctDepartureArrivalDates;

				List<Leg> legs = this.repository.findAllLegs();

				boolean isUnique = legs.stream().noneMatch(l -> l.getFlightNumber().equals(flightNumber) && !l.equals(leg));

				boolean departureArrivalMustBeDifferent = leg.getDepartureAirport().equals(leg.getArrivalAirport());

				if (!isUnique)
					super.state(context, false, "flightNumber", "acme.validation.legs.flightNumber.message");
				if (departureArrivalMustBeDifferent)
					super.state(context, false, "departureAirport", "acme.validation.legs.departureArrivalAirport.message");
				if (!correctCode)
					super.state(context, correct, "flightNumber", "acme.validation.legs.flightNumber.iata.message");
				if (!correctDepartureArrivalDates)
					super.state(context, correct, "scheduledDeparture", "acme.validation.legs.scheduledArrivalDeparture.message");
				if (leg.getFlight() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
					boolean isLegOverlapping = this.repository.isLegOverlapping(leg.getId(), leg.getFlight().getId(), leg.getScheduledDeparture(), leg.getScheduledArrival());
					super.state(context, !isLegOverlapping, "scheduledDeparture", "acme.validation.leg.overlapping-legs.message");
				}
			}
			result = !super.hasErrors(context);

		}
		return result;
	}
}
