
package acme.entities.tracking_logs;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT max(t.percentage) FROM TrackingLog t WHERE t.claim.id = :claimId")
	Double mayorPorcentaje(@Param("claimId") Long claimId);

}
