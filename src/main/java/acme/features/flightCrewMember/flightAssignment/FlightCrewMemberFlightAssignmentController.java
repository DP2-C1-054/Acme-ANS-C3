
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight_assignments.FlightAssignment;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentListCompletedService	listCompletedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListPlannedService		listPlannedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService				showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService			createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService			publishService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService			updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
