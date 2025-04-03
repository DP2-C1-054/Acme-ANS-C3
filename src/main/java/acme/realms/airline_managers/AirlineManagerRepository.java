
package acme.realms.airline_managers;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineManagerRepository extends AbstractRepository {

	@Query("SELECT a FROM AirlineManager a")
	List<AirlineManager> findAllAirlineManagers();
}
