
package acme.features.technician.maintenanceTaskRelation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance_task_relation.MaintenanceTaskRelation;
import acme.entities.tasks.Task;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceTaskRelationCreateService extends AbstractGuiService<Technician, MaintenanceTaskRelation> {

	@Autowired
	private TechnicianMaintenanceTaskRelationRepository repository;


	@Override
	public void authorise() {

		boolean statusTask = true;
		boolean status = false;
		int taskId;
		Task task;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;
		Collection<Task> tasks;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToLink(maintenanceRecord, technician);

		if (super.getRequest().hasData("task", int.class)) {
			taskId = super.getRequest().getData("task", int.class);
			task = this.repository.findTaskById(taskId);

			if (!tasks.contains(task) && taskId != 0)
				statusTask = false;
		}

		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		super.getResponse().setAuthorised(status && statusTask);
	}

	@Override
	public void load() {
		MaintenanceTaskRelation object;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		object = new MaintenanceTaskRelation();
		object.setMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final MaintenanceTaskRelation involves) {

		super.bindObject(involves, "task");

	}

	@Override
	public void validate(final MaintenanceTaskRelation involves) {
		;
	}

	@Override
	public void perform(final MaintenanceTaskRelation involves) {

		this.repository.save(involves);

	}

	@Override
	public void unbind(final MaintenanceTaskRelation involves) {
		Technician technician;
		Collection<Task> tasks;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		SelectChoices choices;
		Dataset dataset;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToLink(maintenanceRecord, technician);
		choices = SelectChoices.from(tasks, "description", involves.getTask());

		dataset = super.unbindObject(involves, "maintenanceRecord");
		dataset.put("maintenanceRecordId", involves.getMaintenanceRecord().getId());
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("aircraftRegistrationNumber", involves.getMaintenanceRecord().getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);

	}
}
