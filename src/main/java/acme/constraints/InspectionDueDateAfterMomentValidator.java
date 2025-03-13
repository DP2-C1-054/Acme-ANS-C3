
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
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
			Date moment = record.getMoment();
			Date inspectionDueDate = record.getInspectionDueDate();
			isAfter = inspectionDueDate.after(moment);
			super.state(context, isAfter, "*", "acme.validation.maintenanceRecord.InspectionDueDateAfterMoment");
		}
		result = !super.hasErrors(context);
		return result;
	}

}
