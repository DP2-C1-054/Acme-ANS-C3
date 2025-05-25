
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
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Claim claim;
		int id;
		AssistanceAgent assistanceAgent;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();

		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = super.getRequest().getPrincipal().hasRealm(assistanceAgent) && claim != null && claim.isDraftMode();
		else {
			int legId = super.getRequest().getData("leg", int.class);

			List<Leg> legs = this.repository.findAllLegs().stream().filter(l -> (MomentHelper.isBefore(l.getScheduledArrival(), MomentHelper.getCurrentMoment()) && !l.isDraftMode() && l.getAircraft().getAirline().equals(assistanceAgent.getAirline())))
				.toList();

			Leg leg = this.repository.findLegById(legId);

			status = (legId == 0 || leg != null && !leg.isDraftMode() && legs.contains(leg)) && super.getRequest().getPrincipal().hasRealm(assistanceAgent) && claim != null && claim.isDraftMode();
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Claim claim;
		int id;
		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		{
			int totalLogs = this.repository.countTrackingLogsByClaimId(claim.getId());
			boolean isValid;
			isValid = totalLogs >= 1;

			super.state(isValid, "*", "acme.validation.claim.publishWithOutLogs.message");
		}
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
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

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg", "draftMode");
		dataset.put("types", choices);
		dataset.put("leg", choices2.getSelected().getKey());
		dataset.put("legs", choices2);

		super.getResponse().addData(dataset);
	}

}
