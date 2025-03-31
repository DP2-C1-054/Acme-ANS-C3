
package acme.features.maintenance;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.technicians.Technician;

@GuiController
public class TechnicianMaintenanceController extends AbstractGuiController<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordListService	listService;

	@Autowired
	private TechnicianMaintenanceRecordShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
