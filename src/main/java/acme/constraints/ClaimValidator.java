
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (claim == null)
			super.state(context, false, "null", "javax.validation.constraints.NotNull.message");
		else {
			boolean isValid = false;
			if (claim.getRegistrationMoment() != null && claim.getLeg() != null && claim.getLeg().getScheduledArrival() != null) {
				Leg leg = claim.getLeg();
				isValid = MomentHelper.isAfter(claim.getRegistrationMoment(), leg.getScheduledArrival()) && !leg.isDraftMode();
			}

			super.state(context, isValid, "leg", "acme.validation.claim.invalid-leg.message");
		}

		result = !super.hasErrors(context);
		return result;

	}
}
