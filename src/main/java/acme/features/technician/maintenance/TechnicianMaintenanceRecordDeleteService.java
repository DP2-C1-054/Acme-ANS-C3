
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
import acme.entities.maintenance_task_relation.MaintenanceTaskRelation;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		mrId = super.getRequest().getData("id", int.class);
		mr = this.repository.findMaintenanceRecordById(mrId);

		technician = mr == null ? null : mr.getTechnician();
		status = mr != null && mr.isDraftMode() && this.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes");

		maintenanceRecord.setAircraft(aircraft);
	}
	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}
	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		Collection<MaintenanceTaskRelation> involves;

		involves = this.repository.findInvolvesByMaintenanceRecordId(maintenanceRecord.getId());
		this.repository.deleteAll(involves);
		this.repository.delete(maintenanceRecord);
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
		dataset.put("technician", maintenanceRecord.getTechnician().getIdentity().getFullName());
		dataset.put("aicraft", selectedAircrafts.getSelected().getKey());
		dataset.put("aircrafts", selectedAircrafts);
		dataset.put("status", choices.getSelected().getKey());
		dataset.put("statuses", choices);

		dataset.put("readonly", false);

		super.getResponse().addData(dataset);

	}

}
