
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.tracking_logs.TrackingLog;
import acme.entities.tracking_logs.TrackingLogStatus;
import acme.realms.assistance_agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int claimId = super.getRequest().getData("claimId", int.class);
		AssistanceAgent agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		Claim claim = this.repository.findClaimById(claimId);

		status = claim.getAssistanceAgent().equals(agent);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		Claim claim;
		int claimId;

		Date lastUpdateMoment = MomentHelper.getCurrentMoment();

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		trackingLog = new TrackingLog();
		trackingLog.setLastUpdateMoment(lastUpdateMoment);
		trackingLog.setClaim(claim);
		trackingLog.setDraftMode(true);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "step", "percentage", "resolution", "status");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		int claimId;
		claimId = super.getRequest().getData("claimId", int.class);
		List<TrackingLog> previousLogs = this.repository.findTrackingLogsByClaimIdOrderedByPercentage(claimId);
		Collection<TrackingLog> logsWith100 = this.repository.findLogsWith100(claimId);

		if (!previousLogs.isEmpty()) {
			TrackingLog lastLog = previousLogs.get(0);
			if (lastLog.getPercentage() != null && trackingLog.getPercentage() != null)
				if (lastLog.getPercentage() == 100.00 && trackingLog.getPercentage() == 100.00) {
					// solo puede repetirse el 100% si está publicada
					if (lastLog.isDraftMode())
						super.state(false, "percentage", "assistance-agent.tracking-log.publish-percentage");
					else if (lastLog.getStatus() != trackingLog.getStatus())
						super.state(false, "percentage", "assistance-agent.tracking-log.status-percentage");
				} else if (lastLog.getPercentage() == trackingLog.getPercentage() && lastLog.getPercentage() != 100.00 && trackingLog.getPercentage() != 100.00)
					// no puede repetirse el porcentaje
					super.state(false, "percentage", "assistance-agent.tracking-log.same-percentage");

		}
		if (logsWith100.size() + 1 > 2)
			super.state(false, "percentage", "assistance-agent.tracking-log.completed-percentage");

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		Date moment = MomentHelper.getCurrentMoment();
		trackingLog.setLastUpdateMoment(moment);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "percentage", "status", "resolution", "draftMode");
		dataset.put("claimId", super.getRequest().getData("claimId", int.class));
		dataset.put("statusOptions", choices);

		super.getResponse().addData(dataset);
	}

}
