
package acme.features.assistanceAgent.claim;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.assistance_agents.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListPendingService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Claim> pendingClaims = this.repository.findAllPendingClaimsByAgentId(agentId);
		Collection<Claim> claimsWithOutTL = this.repository.findAllEmptyClaimsByAgentId(agentId);

		claims = new ArrayList<>(pendingClaims);
		claims.addAll(claimsWithOutTL);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "passengerEmail", "type");
		dataset.put("indicator", claim.indicator());
		super.addPayload(dataset, claim, "registrationMoment", "description", "leg.flightNumber");

		super.getResponse().addData(dataset);
	}

}
