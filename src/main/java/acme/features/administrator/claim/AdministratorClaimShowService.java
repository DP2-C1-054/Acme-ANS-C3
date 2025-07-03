
package acme.features.administrator.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.claims.Status;
import acme.entities.legs.Leg;

@GuiService
public class AdministratorClaimShowService extends AbstractGuiService<Administrator, Claim> {

	@Autowired
	private AdministratorClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Status indicator;
		SelectChoices choices;
		SelectChoices choices2;

		indicator = claim.indicator();
		choices = SelectChoices.from(ClaimType.class, claim.getType());
		Collection<Leg> allLegs;

		allLegs = this.repository.findAllLegs();
		List<Leg> legs = allLegs.stream().filter(l -> (MomentHelper.isBefore(l.getScheduledArrival(), MomentHelper.getCurrentMoment()) && !l.isDraftMode())).toList();

		choices2 = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");
		dataset.put("leg", choices2.getSelected().getKey());
		dataset.put("indicator", indicator);
		dataset.put("types", choices);
		dataset.put("legs", choices2);

		super.getResponse().addData(dataset);
	}
}
