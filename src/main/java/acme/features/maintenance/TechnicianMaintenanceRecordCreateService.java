
package acme.features.maintenance;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceStatus;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int aircraftId;
		Aircraft aircraft;
		Date currentDate;

		aircraftId = super.getRequest().getData("assignedAircraftId", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		currentDate = MomentHelper.getCurrentMoment();
		status = aircraft != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record;
		Technician technician;
		int aircraftId;
		Aircraft aircraft;
		Date currentDate;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		aircraftId = super.getRequest().getData("assignedAircraftId", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		currentDate = MomentHelper.getCurrentMoment();

		record = new MaintenanceRecord();
		record.setMoment(currentDate);
		record.setStatus(MaintenanceStatus.PENDING);
		record.setEstimatedCost(new Money());
		record.setNotes("");
		record.setAssignedTechnician(technician);

		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintenanceRecord record) {
		super.bindObject(record, "moment", "status", "estimatedCost", "notes");
	}

	@Override
	public void validate(final MaintenanceRecord record) {
		;
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;

		dataset = super.unbindObject(record, "moment", "status", "estimatedCost", "notes");

		super.getResponse().addData(dataset);
	}

}
