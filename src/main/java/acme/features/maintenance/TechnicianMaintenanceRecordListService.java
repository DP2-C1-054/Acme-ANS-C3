
package acme.features.maintenance;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> object;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		object = this.repository.findMaintenanceRecordsByTechnician(technicianId);

		super.getBuffer().addData(object);

	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost");
		dataset.put("aircraft", maintenanceRecord.getAssignedAircraft().getAircraftDetails());

		super.getResponse().addData(dataset);

	}

}
