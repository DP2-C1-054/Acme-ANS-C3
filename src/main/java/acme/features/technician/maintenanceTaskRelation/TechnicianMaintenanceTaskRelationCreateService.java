
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
		boolean status;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		mrId = super.getRequest().getData("maintenanceRecordId", int.class);
		mr = this.repository.findMaintenanceRecordById(mrId);

		technician = mr == null ? null : mr.getTechnician();
		status = mr != null && mr.isDraftMode() && this.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record;
		MaintenanceTaskRelation involves;
		int recordId;
		recordId = super.getRequest().getData("maintenanceRecordId", int.class);
		record = this.repository.findMaintenanceRecordById(recordId);
		involves = new MaintenanceTaskRelation();
		involves.setMaintenanceRecord(record);
		super.getBuffer().addData(involves);

	}

	@Override
	public void bind(final MaintenanceTaskRelation involves) {
		int taskId;
		Task task;

		taskId = super.getRequest().getData("task", int.class);
		task = this.repository.findTaskById(taskId);

		super.bindObject(involves);
		involves.setTask(task);

	}

	@Override
	public void validate(final MaintenanceTaskRelation involves) {
		boolean notPublished = true;
		MaintenanceRecord mr = involves.getMaintenanceRecord();
		if (mr != null && !mr.isDraftMode())
			notPublished = false;
		super.state(notPublished, "maintenanceRecord", "acme.validation.involves.invalid-involves-publish.message");
	}

	@Override
	public void perform(final MaintenanceTaskRelation involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final MaintenanceTaskRelation involves) {
		Collection<Task> tasks;
		Dataset dataset;
		SelectChoices choices;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		int technicianId;
		Technician technician;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		technician = this.repository.findTechnicianById(technicianId);

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		tasks = this.repository.findValidTasksToLink(maintenanceRecord, technician);
		choices = SelectChoices.from(tasks, "description", involves.getTask());
		dataset = super.unbindObject(involves);
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("maintenanceRecordId", maintenanceRecordId);
		super.getResponse().addData(dataset);
	}
}
