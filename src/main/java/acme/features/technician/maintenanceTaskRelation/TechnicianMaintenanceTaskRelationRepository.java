
package acme.features.technician.maintenanceTaskRelation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance_task_relation.MaintenanceTaskRelation;
import acme.entities.tasks.Task;
import acme.realms.technicians.Technician;

@Repository
public interface TechnicianMaintenanceTaskRelationRepository extends AbstractRepository {

	@Query("select mtr from MaintenanceTaskRelation mtr where mtr.maintenanceRecord.technician.id =: id ")
	Collection<MaintenanceTaskRelation> findMaintenanceTaskRelationsByTechnicianId(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :maintenanceRecordId")
	MaintenanceRecord findMaintenanceRecordById(int maintenanceRecordId);

	@Query("select t from Task t where t.id = :taskId")
	Task findTaskById(int taskId);

	@Query("select t from Task t where t not in (select mtr.task from MaintenanceTaskRelation mtr where mtr.maintenanceRecord = :maintenanceRecord) and (t.draftMode = false or t.technician = :technician)")
	Collection<Task> findValidTasksToLink(MaintenanceRecord maintenanceRecord, Technician technician);

	@Query("select t from Task t where t in (select mtr.task from MaintenanceTaskRelation mtr where mtr.maintenanceRecord = :maintenanceRecord)")
	Collection<Task> findValidTasksToUnlink(MaintenanceRecord maintenanceRecord);

	@Query("select mtr from MaintenanceTaskRelation mtr where mtr.maintenanceRecord = :maintenanceRecord")
	Collection<MaintenanceTaskRelation> findMaintenanceTaskRelationsByMaintenanceRecord(MaintenanceRecord maintenanceRecord);

	@Query("select mtr from MaintenanceTaskRelation mtr where mtr.maintenanceRecord = :maintenanceRecord and mtr.task = :task")
	MaintenanceTaskRelation findMaintenanceTaskRelationsByMaintenanceRecordAndTask(MaintenanceRecord maintenanceRecord, Task task);

	@Query("select mr from MaintenanceRecord mr where mr.technician.id =: id")
	Collection<MaintenanceRecord> findMaintenanceRecordByTechnicianId(int id);

	@Query("select t from Technician t where t.id = :id")
	Technician findTechnicianById(int id);

}
