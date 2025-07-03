
package acme.features.any.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignments.AssignmentStatus;
import acme.entities.flight_assignments.Duty;
import acme.entities.flight_assignments.FlightAssignment;

@GuiService
public class AnyFlightAssignmentShowService extends AbstractGuiService<Any, FlightAssignment> {

	@Autowired
	private AnyFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignment) {
		Dataset dataset = super.unbindObject(completedFlightAssignment, "duty", "moment", "status", "remarks", "allocatedFlightCrewMember", "leg", "draftMode");

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, completedFlightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, completedFlightAssignment.getStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsByAirlineId(completedFlightAssignment.getAllocatedFlightCrewMember().getAirline().getId()), "flightNumber", completedFlightAssignment.getLeg());
		dataset.put("legChoices", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
