
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.tracking_logs.TrackingLog;
import acme.realms.assistance_agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		Claim claim;
		boolean status;
		int claimId;

		AssistanceAgent agent;

		claimId = super.getRequest().getData("claimId", int.class);

		claim = this.repository.findClaimById(claimId);
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent()) && claim != null && claim.getAssistanceAgent().equals(agent);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int claimId;

		claimId = super.getRequest().getData("claimId", int.class);
		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claimId);

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "percentage", "status");
		super.addPayload(dataset, trackingLog, "step", "resolution");

		super.getResponse().addData(dataset);
	}

}
