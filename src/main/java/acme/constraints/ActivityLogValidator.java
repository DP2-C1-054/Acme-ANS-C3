
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

			if (flightAssignment.isDraftMode()) {
				super.state(context, false, "flightAssignment", "acme.validation.activity-log.flight-assignment-not-published.message");
				return false;
			}

			if (!AssignmentStatus.CONFIRMED.equals(flightAssignment.getStatus())) {
				super.state(context, false, "flightAssignment", "acme.validation.activity-log.flight-assignment-not-confirmed.message");
				return false;
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
