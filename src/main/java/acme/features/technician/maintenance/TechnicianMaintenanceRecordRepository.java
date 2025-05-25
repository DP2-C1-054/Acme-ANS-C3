
package acme.features.technician.maintenance;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance_task_relation.MaintenanceTaskRelation;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr")
	Collection<MaintenanceRecord> findAllMaintenaceRecord();

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId")
	Collection<MaintenanceRecord> findAllMaintenanceRecordByTechnicianId(int technicianId);

	@Query("select mr.aircraft from MaintenanceRecord mr where mr.id = id")
	Aircraft findAircraftByMaintenanceRecordId(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id=:id")
	Aircraft findAircraftById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mtr from MaintenanceTaskRelation mtr where mtr.maintenanceRecord.id = :id")
	Collection<MaintenanceTaskRelation> findInvolvesByMaintenanceRecordId(int id);

	@Query("select mtr.task from MaintenanceTaskRelation mtr where mtr.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

	@Query("Select mr from MaintenanceRecord mr where mr.draftMode = false")
	Collection<MaintenanceRecord> findPublishedMaintenanceRecords();

}
