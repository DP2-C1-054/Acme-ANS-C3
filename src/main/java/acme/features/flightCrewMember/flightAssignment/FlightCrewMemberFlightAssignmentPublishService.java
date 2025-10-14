
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignments.AssignmentStatus;
import acme.entities.flight_assignments.Duty;
import acme.entities.flight_assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.entities.legs.Leg.Status;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {

		boolean status = true;

		if (super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			Optional<FlightAssignment> optional = this.repository.findFlightAssignmentById(id);
			if (optional.isPresent()) {
				FlightAssignment fa = optional.get();
				if (!fa.isDraftMode())
					status = false;
			} else
				status = false;
		}

		super.getResponse().setAuthorised(status);

		if (super.getRequest().getMethod().equals("POST")) {
			if (super.getRequest().hasData("leg")) {
				int legId = super.getRequest().getData("leg", int.class);
				if (legId != 0) {
					Optional<Leg> optionalLeg = this.repository.findLegById(legId);
					if (optionalLeg.isEmpty())
						status = false;
					else {
						Leg leg = optionalLeg.get();
						Collection<Status> legStatuses = List.of(Status.ON_TIME, Status.DELAYED);
						Collection<Leg> availableLegs = this.repository.findAllLegsAvailables(legStatuses);
						if (!availableLegs.contains(leg))
							status = false;
					}
				}
			}
			if (status && super.getRequest().hasData("duty")) {
				String duty = super.getRequest().getData("duty", String.class);
				status = duty.equals("0") || duty.equals("PILOT") || duty.equals("CO_PILOT") || duty.equals("LEAD_ATTENDANT") || duty.equals("CABIN_ATTENDANT");
			}
			if (status && super.getRequest().hasData("status")) {
				String currentStatus = super.getRequest().getData("status", String.class);
				status = currentStatus.equals("0") || currentStatus.equals("CONFIRMED") || currentStatus.equals("PENDING") || currentStatus.equals("CANCELLED");
			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		FlightAssignment flightAssignment = new FlightAssignment();
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id).get();
		flightAssignment.setMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(flightAssignment);
		;
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {

		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId).orElse(null);

		super.bindObject(flightAssignment, "leg", "status", "duty", "remarks");
		flightAssignment.setLeg(leg);

		flightAssignment.setMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "moment", "remarks", "duty", "status", "draftMode");

		Collection<Status> legStatuses = List.of(Status.ON_TIME, Status.DELAYED);
		Collection<Leg> legList = this.repository.findAllLegsAvailables(legStatuses);
		SelectChoices legs = SelectChoices.from(legList, "flightNumber", flightAssignment.getLeg());
		dataset.put("legs", legs);

		SelectChoices duties;
		duties = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		dataset.put("duties", duties);

		SelectChoices statuses;
		statuses = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());
		dataset.put("statuses", statuses);

		dataset.put("memberId", super.getRequest().getPrincipal().getActiveRealm().getId());

		super.getResponse().addData(dataset);
	}

}
