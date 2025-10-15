
package acme.features.airline_managers.legs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.flight_assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AirlineManagerLegDeleteService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status = false;

		if (super.getRequest().hasData("id")) {
			int legId = super.getRequest().getData("id", int.class);
			int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

			Optional<Leg> optionalLeg = this.repository.findByLegId(legId);

			if (optionalLeg.isPresent()) {
				Leg leg = optionalLeg.get();

				boolean isOwner = this.repository.findByIdAndManagerId(leg.getFlight().getId(), managerId).isPresent();
				boolean isDraft = leg.isDraftMode();

				status = isOwner && isDraft;
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByLegId(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		;
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		List<FlightAssignment> flightAssignments;
		List<Claim> claims;

		flightAssignments = this.repository.findFlightAssignmentsByLegId(leg.getId());
		flightAssignments.stream().forEach(f -> this.repository.deleteAll(this.repository.findActivityLogsByFlightAssignmentId(f.getId())));
		claims = this.repository.findClaimsByLegId(leg.getId());
		claims.stream().forEach(c -> this.repository.deleteAll(this.repository.findTrackingLogByClaimId(c.getId())));

		this.repository.deleteAll(flightAssignments);
		this.repository.deleteAll(claims);
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		;
	}

}
