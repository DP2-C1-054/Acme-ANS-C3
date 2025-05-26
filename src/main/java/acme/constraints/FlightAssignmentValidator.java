
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flight_assignments.AssignmentStatus;
import acme.entities.flight_assignments.Duty;
import acme.entities.flight_assignments.FlightAssignment;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberFlightAssignmentRepository;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (flightAssignment == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		var flightCrewMember = flightAssignment.getAllocatedFlightCrewMember();
		var leg = flightAssignment.getLeg();
		var status = flightAssignment.getStatus();
		var duty = flightAssignment.getDuty();

		if (leg != null) {

			boolean legPublished = !leg.isDraftMode();
			super.state(context, legPublished, "leg", "acme.validation.flight-assignment.leg-not-published.message");

			if (status != null && flightCrewMember != null) {

				boolean flightPublishedAndConfirmed = !flightAssignment.isDraftMode() && status.equals(AssignmentStatus.CONFIRMED);

				if (flightPublishedAndConfirmed) {
					boolean flightCrewMemberAvailable = this.repository.isCrewMemberAvailable(flightAssignment.getId(), flightCrewMember.getId(), leg.getScheduledDeparture(), leg.getScheduledArrival());

					super.state(context, flightCrewMemberAvailable, "leg", "acme.validation.flight-assignment.flight-crew-member-unavailable.message");
				}

				if (duty != null) {

					boolean flightPublishedAndNotCancelled = !flightAssignment.isDraftMode() && (status.equals(AssignmentStatus.CONFIRMED) || status.equals(AssignmentStatus.PENDING));

					if (flightPublishedAndNotCancelled) {
						boolean legMemberDutyUnique = this.repository.isLegMemberDutyUnique(flightAssignment.getId(), leg.getId(), flightCrewMember.getId(), duty);

						super.state(context, legMemberDutyUnique, "duty", "acme.validation.flight-assignment.member-already-in-leg.message");
					}

					if (flightPublishedAndConfirmed)
						if (duty.equals(Duty.PILOT)) {
							boolean pilotDutyFree = this.repository.isDutyFree(flightAssignment.getId(), leg.getId(), Duty.PILOT);

							super.state(context, pilotDutyFree, "duty", "acme.validation.flight-assignment.pilot.message");
						}

						else if (duty.equals(Duty.CO_PILOT)) {
							boolean copilotDutyFree = this.repository.isDutyFree(flightAssignment.getId(), leg.getId(), Duty.CO_PILOT);

							super.state(context, copilotDutyFree, "duty", "acme.validation.flight-assignment.copilot.message");
						}
				}
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
