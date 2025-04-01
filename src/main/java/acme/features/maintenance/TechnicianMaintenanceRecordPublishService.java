
package acme.features.maintenance;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {

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

	@Override
	public void perform(final MaintenanceRecord record) {

	}

	@Override
	public void unbind(final MaintenanceRecord record) {

	}

}
