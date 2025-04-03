
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance_task_relation.MaintenanceTaskRelation;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mtr.task from MaintenanceTaskRelation mtr where mtr.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMasterId(int id);

	@Query("select mtr.maintenanceRecord from MaintenanceTaskRelation mtr where mtr.task.id = :id")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTaskId(int id);

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.draftMode = false")
	Collection<Task> findPublishedTasks();

	@Query("select mtr from MaintenanceTaskRelation mtr where mtr.task.id = :id")
	Collection<MaintenanceTaskRelation> findInvolvesByTaskId(int id);

	@Query("select mtr.task from MaintenanceTaskRelation mtr where mtr.maintenanceRecord = :maintenanceRecord")
	Collection<Task> findInvolvesByMaintenanceRecord(MaintenanceRecord maintenanceRecord);

}
