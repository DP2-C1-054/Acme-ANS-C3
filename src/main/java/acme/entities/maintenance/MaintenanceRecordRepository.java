
package acme.entities.maintenance;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface MaintenanceRecordRepository extends AbstractRepository {

	@Query("SELECT mr FROM MaintenanceRecord mr WHERE mr.assignedTechnician.id = :technicianId")
	List<MaintenanceRecord> findByTechnicianId(Integer technicianId);

}
