
package acme.entities.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TakesRepository extends AbstractRepository {

	@Query("SELECT t FROM Takes t")
	List<Takes> findAllTakes();
}
