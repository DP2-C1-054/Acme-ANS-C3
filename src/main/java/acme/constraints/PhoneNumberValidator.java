
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

	@Override
	public void initialize(final ValidPhoneNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {
		if (phoneNumber == null || phoneNumber.isEmpty())
			return true;

		return phoneNumber.matches("^\\+?\\d{6,15}$");
	}
}
