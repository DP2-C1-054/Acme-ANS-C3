
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
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
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	public boolean authoriseFlightAssignment(final FlightAssignment flightAssignment) {
		return flightAssignment.isDraftMode();
	}

	@Override
	public void authorise() {

		boolean status;
		int assignmentId;
		Optional<FlightAssignment> flightAssignment;

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		assignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findByIdAndFlightCrewMemberId(assignmentId, flightCrewMember.getId());
		status = flightAssignment.map(this::authoriseFlightAssignment).orElse(false);

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

		Collection<Leg> legList = this.repository.findAllLegs();
		SelectChoices legs = SelectChoices.from(legList, "flightNumber", flightAssignment.getLeg());
		dataset.put("legs", legs);

		SelectChoices duties;
		duties = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		dataset.put("duties", duties);

		SelectChoices statuses;
		statuses = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());
		dataset.put("statuses", statuses);

		super.getResponse().addData(dataset);
	}

}
