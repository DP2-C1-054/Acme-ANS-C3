
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activity_logs.ActivityLog;
import acme.entities.flight_assignments.AssignmentStatus;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (activityLog == null)
			return true;

		var flightAssignment = activityLog.getFlightAssignment();

		if (flightAssignment != null) {

			boolean assignmentIsPublished = !flightAssignment.isDraftMode();
			super.state(context, assignmentIsPublished, "flightAssignment", "acme.validation.activity-log.flight-assignment-not-published.message");

			boolean assignmentIsConfirmed = AssignmentStatus.CONFIRMED.equals(flightAssignment.getStatus());
			super.state(context, assignmentIsConfirmed, "flightAssignment", "acme.validation.activity-log.flight-assignment-not-confirmed.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
