
package acme.entities.tracking_logs;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT tl FROM TrackingLog ORDER BY tl.percentage DESC LIMIT 1")
	Double mayorPorcentaje();

}
