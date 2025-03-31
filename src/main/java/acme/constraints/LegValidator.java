
package acme.constraints;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
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

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correct;

			String airlineIataCode = leg.getAircraft().getAirline().getIataCode();

			if (airlineIataCode == null || leg.getFlightNumber() == null || leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null)
				super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

			Date currentDate = MomentHelper.getCurrentMoment();

			boolean correctCode = leg.getFlightNumber().substring(0, 3).equalsIgnoreCase(airlineIataCode);
			boolean correctDepartureArrivalDates = leg.getScheduledDeparture().compareTo(leg.getScheduledArrival()) < 0;
			boolean correctDate = leg.getScheduledDeparture().compareTo(currentDate) > 0 && leg.getScheduledArrival().compareTo(currentDate) > 0;

			correct = correctCode && correctDepartureArrivalDates && correctDate;

			String flightNumber = leg.getFlightNumber();

			List<Leg> legs = this.repository.findAllLegs();
			boolean isUnique = legs.stream().noneMatch(l -> l.getFlightNumber().equals(flightNumber) && l.equals(legs));

			if (!isUnique)
				super.state(context, false, "*", "acme.validation.leg.flight-number.message");
			if (!correctCode)
				super.state(context, correct, "*", "acme.validation.legs.flight-number.message");
			if (!correctDate)
				super.state(context, correct, "*", "acme.validation.legs.current-dates.message");
			if (!correctDepartureArrivalDates)
				super.state(context, correct, "*", "acme.validation.legs.departure-arrival-date.message");

		}
		result = !super.hasErrors(context);

		return result;
	}
}
