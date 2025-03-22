
package acme.entities.aircrafts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository {

	@Query("SELECT a FROM Aircraft a")
	List<Aircraft> findAllAircrafts();

}
