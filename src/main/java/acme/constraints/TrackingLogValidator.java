
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.tracking_logs.TrackingLog;
import acme.entities.tracking_logs.TrackingLogRepository;
import acme.entities.tracking_logs.TrackingLogStatus;

@Validator
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
			TrackingLogStatus trackingLogStatus = trackingLog.getStatus();
			Double percentage = trackingLog.getPercentage();
			boolean correctStatus = true;

			if (trackingLogStatus != null && percentage != null) {
				if (percentage == 100.00 && trackingLogStatus.equals(TrackingLogStatus.PENDING))
					correctStatus = false;
				if (percentage != 100.00 && !trackingLogStatus.equals(TrackingLogStatus.PENDING))
					correctStatus = false;
			}

			super.state(context, correctStatus, "status", "acme.validation.tracking-log.status.message");

			// /////////////////////////////////////////////////////

			String resolution = trackingLog.getResolution();
			boolean correctResolution = true;

			if (trackingLogStatus != null)
				if (!trackingLogStatus.equals(TrackingLogStatus.PENDING) && StringHelper.isEmpty(resolution)) {
					super.state(context, false, "resolution", "javax.validation.constraints.NotNull.message");
					correctResolution = false;
				}

			super.state(context, correctResolution, "resolution", "acme.validation.tracking-log.resolution.message");

			// /////////////////////////////////////////////////////

			boolean estaOrdenada = this.aux_func(trackingLog);
			if (!estaOrdenada)
				super.state(context, false, "percentage", "acme.validation.tracking-log.percentage-creciente.message");

		}

		result = !super.hasErrors(context);

		return result;
	}

	private boolean aux_func(final TrackingLog newTrackingLog) {
		if (newTrackingLog != null && newTrackingLog.getClaim() != null && newTrackingLog.getPercentage() != null) {
			List<TrackingLog> existingLogs = this.repository.findTrackingLogsOrderByMomentASC(newTrackingLog.getClaim().getId());

			if (existingLogs.isEmpty())
				return true;

			TrackingLog previousLog = existingLogs.get(existingLogs.size() - 1);

			Integer newId = newTrackingLog.getId();
			if (newId != null) {
				int currentIndex = -1;
				for (int i = 0; i < existingLogs.size(); i++)
					if (newId.equals(existingLogs.get(i).getId())) {
						currentIndex = i;
						break;
					}
				if (currentIndex > 0)
					previousLog = existingLogs.get(currentIndex - 1);
				else if (currentIndex == 0)
					return true;
			}

			if (previousLog != null)
				if (newTrackingLog.getPercentage() < previousLog.getPercentage())
					return false;
				else if (newTrackingLog.getPercentage() > previousLog.getPercentage())
					return true;
		}
		return false;

	}

}
