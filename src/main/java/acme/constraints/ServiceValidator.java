
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.entities.service.Service;
import acme.entities.service.ServiceRepository;

@Validator
public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	@Autowired
	private ServiceRepository repository;


	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (service == null)
			super.state(context, false, "", "javax.validation.constraints.NotNull.message");
		else {
			boolean codeContainsActualYear = true;
			boolean hasDiscountAndCode = true;
			if (service.getDiscount() != null && !StringHelper.isBlank(service.getPromotionCode()) && service.getPromotionCode().length() >= 2) {
				List<Service> services = this.repository.findAllServices();
				String discountCode = service.getPromotionCode();
				boolean isUnique = services.stream().noneMatch(s -> s.getPromotionCode() != null && s.getPromotionCode().equals(discountCode) && !s.equals(service));
				int year = MomentHelper.getCurrentMoment().getYear() - 100;
				String actualYear = String.valueOf(year);
				String yearInCode = discountCode.substring(discountCode.length() - 2);
				if (!isUnique)
					super.state(context, false, "*", "acme.validation.service.uniquePromotionCode.message");
				codeContainsActualYear = StringHelper.isEqual(yearInCode, actualYear, true);
			} else if (service.getDiscount() != null && service.getPromotionCode() == null || service.getDiscount() == null && service.getPromotionCode() != null) {
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
