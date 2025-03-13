
package acme.constraints;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.service.Service;

public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean codeContainsActualYear;
			boolean hasDiscountAndCode;
			try {
				if (service.getDiscount() != null && service.getPromotionCode() != null) {
					String discountCode = service.getPromotionCode();

					Date currentMoment = MomentHelper.getCurrentMoment();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(currentMoment);
					String actualYear = String.valueOf(calendar.get(Calendar.YEAR)).substring(-2);
					String yearInCode = discountCode.substring(-2);

					codeContainsActualYear = yearInCode == actualYear;
					hasDiscountAndCode = true;
				} else if (service.getDiscount() != null && service.getPromotionCode() == null || service.getDiscount() == null && service.getPromotionCode() != null) {
					codeContainsActualYear = false;
					hasDiscountAndCode = false;
				} else {
					codeContainsActualYear = true;
					hasDiscountAndCode = true;
				}
			} catch (Error e) {
				codeContainsActualYear = false;
				hasDiscountAndCode = false;
			}
			super.state(context, codeContainsActualYear, "*", "acme.validation.service.promotionCode.message");
			super.state(context, hasDiscountAndCode, "*", "acme.validation.service.promotionCodeAndDiscount.message");
		}
		result = !super.hasErrors(context);
		return result;
	}

}
