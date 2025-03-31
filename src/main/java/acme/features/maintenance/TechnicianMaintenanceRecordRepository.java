
package acme.features.maintenance;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("SELECT mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("Select mr from MaintenanceRecord mr where mr.assignedTechnician.id = :id")
	List<MaintenanceRecord> findMaintenanceRecordsByTechnician(int id);

	@Query("SELECT a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

}
