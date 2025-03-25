
package acme.realms.technicians;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TechnicianRepository extends AbstractRepository {

	@Query("SELECT t FROM Technician t")
	List<Technician> findAllTechnicians();

	@Query("SELECT t.licenseNumber FROM Technician t")
	List<String> findAllLicenseNumber();

	@Query("SELECT COUNT(t) > 0 FROM Technician t WHERE t.licenseNumber = :licenseNumber")
	boolean existsByLicenseNumber(@Param("licenseNumber") String licenseNumber);

}
