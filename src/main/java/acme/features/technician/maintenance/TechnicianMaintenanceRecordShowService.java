
package acme.features.technician.maintenance;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceStatus;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		mrId = super.getRequest().getData("id", int.class);
		mr = this.repository.findMaintenanceRecordById(mrId);

		technician = mr == null ? null : mr.getTechnician();
		status = mr != null && (mr.isDraftMode() == false || super.getRequest().getPrincipal().hasRealm(technician));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord mr;
		int id;

		id = super.getRequest().getData("id", int.class);
		mr = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(mr);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices selectedAircrafts;
		Collection<Aircraft> aircrafts;

		choices = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());

		aircrafts = this.repository.findAllAircrafts();
		selectedAircrafts = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");

		dataset.put("aicraft", selectedAircrafts.getSelected().getKey());
		dataset.put("aircrafts", selectedAircrafts);
		dataset.put("statuses", choices);
		dataset.put("maintenanceRecordId", maintenanceRecord.getId());

		super.getResponse().addData(dataset);
	}

}
