
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.realms.assistance_agents.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;
		String method;
		Leg leg;
		int legId;
		AssistanceAgent assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findValidLegById(assistanceAgent.getAirline(), legId);

			status = legId == 0 || leg != null && !leg.isDraftMode();

		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		claim = new Claim();

		claim.setAssistanceAgent(assistanceAgent);
		claim.setDraftMode(true);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> allLegs;
		SelectChoices choices;
		SelectChoices choices2;
		Dataset dataset;
		AssistanceAgent assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		choices = SelectChoices.from(ClaimType.class, claim.getType());

		allLegs = this.repository.findAllLegs();
		List<Leg> legs = allLegs.stream().filter(l -> (MomentHelper.isBefore(l.getScheduledArrival(), MomentHelper.getCurrentMoment()) && !l.isDraftMode() && l.getAircraft().getAirline().equals(assistanceAgent.getAirline()))).toList();
		choices2 = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");
		dataset.put("types", choices);

		dataset.put("legs", choices2);

		super.getResponse().addData(dataset);
	}

}
