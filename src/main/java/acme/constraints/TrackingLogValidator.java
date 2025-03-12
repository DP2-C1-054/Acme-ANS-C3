
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
			Status trackingLogStatus = trackingLog.getStatus();

			{
				boolean correctStatus = true;
				double percentage = trackingLog.getPercentage();
				if (percentage == 100.00 && trackingLogStatus.equals(Status.PENDING))
					correctStatus = false;
				if (percentage != 100.00 && !trackingLogStatus.equals(Status.PENDING))
					correctStatus = false;

				super.state(context, correctStatus, "*", "acme.validation.tracking-log.status.message");

			}
			{
				boolean correctPercentage = true;
				double maxPercentage = this.repository.findMayorPorcentaje(trackingLog.getClaim().getId()).orElse(0.0);

				if (maxPercentage > trackingLog.getPercentage())
					correctPercentage = false;
				super.state(context, correctPercentage, "*", "acme.validation.tracking-log.resolution.message");
			}
			{
				String resolution = trackingLog.getResolution();

				boolean correctResolution = true;
				if (!trackingLogStatus.equals(Status.PENDING) && resolution.isEmpty())
					correctResolution = false;
				super.state(context, correctResolution, "*", "acme.validation.tracking-log.resolution.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
