
package acme.features.technician.maintenanceTaskRelation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenance_task_relation.MaintenanceTaskRelation;
import acme.realms.technicians.Technician;

@GuiController
public class TechnicianMaintenanceTaskRelationController extends AbstractGuiController<Technician, MaintenanceTaskRelation> {

	@Autowired
	private TechnicianMaintenanceTaskRelationCreateService createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
	}

}
