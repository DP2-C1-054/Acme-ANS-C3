
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PromotionCodeValidator implements ConstraintValidator<ValidPromotionCode, String> {

	@Override
	public void initialize(final ValidPromotionCode constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String code, final ConstraintValidatorContext context) {
		if (code == null || code.isEmpty())
			return true;

		return code.matches("^[A-Z]{4}-[0-9]{2}$");
	}
}
