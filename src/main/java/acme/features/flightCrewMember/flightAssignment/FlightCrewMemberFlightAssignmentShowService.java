
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignments.AssignmentStatus;
import acme.entities.flight_assignments.Duty;
import acme.entities.flight_assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		Optional<FlightAssignment> flightAssignment = this.repository.findByIdAndFlightCrewMemberId(flightAssignmentId, flightCrewMember.getId());

		boolean status = flightAssignment.isPresent();
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int flightAssignmentId;
		FlightAssignment flightAssignment;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId).get();

		super.getBuffer().addData(flightAssignment);
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
