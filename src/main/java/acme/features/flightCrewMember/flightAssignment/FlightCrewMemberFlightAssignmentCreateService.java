
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

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
import acme.realms.flight_crew_members.AvailabilityStatus;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = super.getRequest().getPrincipal().getActiveRealm() instanceof FlightCrewMember;

		if (status == true)
			super.getResponse().setAuthorised(flightCrewMember.getAvailability().equals(AvailabilityStatus.AVAILABLE));
		else
			super.getResponse().setAuthorised(false);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		FlightAssignment flightAssignment = new FlightAssignment();
		flightAssignment.setDraftMode(true);
		flightAssignment.setAllocatedFlightCrewMember(flightCrewMember);
		flightAssignment.setMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(flightAssignment);
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
