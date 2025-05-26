
package acme.features.flightCrewMember.activityLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activity_logs.ActivityLog;
import acme.realms.flight_crew_members.FlightCrewMember;

@GuiController
public class FlightCrewMemberActivityLogController extends AbstractGuiController<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogListService		listService;

	@Autowired
	private FlightCrewMemberActivityLogShowService		showService;

	@Autowired
	private FlightCrewMemberActivityLogCreateService	createService;

	@Autowired
	private FlightCrewMemberActivityLogUpdateService	updateService;

	@Autowired
	private FlightCrewMemberActivityLogPublishService	publishService;

	@Autowired
	private FlightCrewMemberActivityLogDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
