
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenance.MaintenanceRecord;

@Validator
public class InspectionDueDateAfterMomentValidator extends AbstractValidator<ValidInspectionDueDateAfterMoment, MaintenanceRecord> {

	@Override
	protected void initialise(final ValidInspectionDueDateAfterMoment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord record, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (record == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean isAfter;
			if (record.getMaintenanceMoment() != null && record.getNextInspectionDue() != null) {

				Date moment = record.getMaintenanceMoment();
				Date inspectionDueDate = record.getNextInspectionDue();
				isAfter = MomentHelper.isAfter(inspectionDueDate, moment);
				super.state(context, isAfter, "nextInspectionDue", "acme.validation.maintenanceRecord.InspectionDueDateAfterMoment");
			}
		}
		result = !super.hasErrors(context);
		return result;
	}

}
