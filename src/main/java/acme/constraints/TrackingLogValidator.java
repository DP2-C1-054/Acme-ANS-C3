
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.tracking_logs.Status;
import acme.entities.tracking_logs.TrackingLog;
import acme.entities.tracking_logs.TrackingLogRepository;

public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private TrackingLogRepository repository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Status trackingLogIndicator = trackingLog.getIndicator();

			{
				double percentage = trackingLog.getPercentage();
				boolean rest1 = percentage < 100.00 && trackingLogIndicator.equals(Status.PENDING);
				boolean rest2 = percentage >= 100.00 && !trackingLogIndicator.equals(Status.PENDING);

				boolean correctIndicator = rest1 || rest2;

				super.state(context, correctIndicator, "*", "acme.validation.tracking-log.indicator.message");

			}

			{
				String resolution = trackingLog.getResolution();
				boolean completa = !trackingLogIndicator.equals(Status.PENDING) && !resolution.isEmpty();
				boolean incompleta = trackingLogIndicator.equals(Status.PENDING) && resolution.isEmpty();

				boolean correctResolution = completa || incompleta;

				super.state(context, correctResolution, "*", "acme.validation.tracking-log.resolution.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
