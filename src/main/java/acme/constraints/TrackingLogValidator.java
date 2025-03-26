
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.Claim;
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

			super.state(context, correctStatus, "*", "acme.validation.tracking-log.status.message");

			///////////////////////////////////////////////////////

			String resolution = trackingLog.getResolution();
			boolean correctResolution = true;

			if (trackingLogStatus != null)
				if (!trackingLogStatus.equals(TrackingLogStatus.PENDING) && StringHelper.isEmpty(resolution)) {
					super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
					correctResolution = false;
				}

			super.state(context, correctResolution, "*", "acme.validation.tracking-log.resolution.message");

			///////////////////////////////////////////////////////

			Claim claim = trackingLog.getClaim();
			if (claim != null) {
				List<TrackingLog> TLlist = this.repository.findTrackingLogsOrderByMoment(claim.getId());
				if (TLlist.size() >= 1) {
					boolean estaOrdenada = true;
					for (int i = 0; i < TLlist.size() - 1; i++)
						if (TLlist.get(i).getPercentage() < TLlist.get(i + 1).getPercentage()) {
							estaOrdenada = false;
							break;
						}

					if (!estaOrdenada)
						super.state(context, false, "*", "acme.validation.tracking-log.percentage-creciente.message");
				}

			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
