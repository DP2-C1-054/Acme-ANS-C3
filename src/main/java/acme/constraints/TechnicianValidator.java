
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.technicians.Technician;
import acme.realms.technicians.TechnicianRepository;

public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Autowired
	private TechnicianRepository technicianRepository;


	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician value, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (value == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			List<Technician> existingTechnicians = this.technicianRepository.findAllTechnicians();
			if (value.getLicenseNumber() != null) {
				boolean isUnique = existingTechnicians.stream().noneMatch(t -> t.getLicenseNumber().equals(value.getLicenseNumber()) && t != value);

				if (!isUnique)
					super.state(context, false, "*", "acme.validation.technician.license-number-not-unique.message");

			}

		}
		result = !super.hasErrors(context);
		return result;

	}

}
