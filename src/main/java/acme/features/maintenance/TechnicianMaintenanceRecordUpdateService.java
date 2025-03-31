
package acme.features.maintenance;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		Aircraft aircraft;

		recordId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(recordId);
	}

	@Override
	public void load() {

	}

	@Override
	public void bind(final MaintenanceRecord record) {

	}

	@Override
	public void validate(final MaintenanceRecord record) {
		;
	}

	public void perform() {

	}
}
