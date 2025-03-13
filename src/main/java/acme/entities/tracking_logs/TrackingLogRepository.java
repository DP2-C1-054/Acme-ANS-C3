
package acme.entities.tracking_logs;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT max(t.percentage) FROM TrackingLog t WHERE t.claim.id = :claimId")
	Optional<Double> findMayorPorcentaje(@Param("claimId") int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	List<TrackingLog> findTrackingLogsByClaimId(@Param("claimId") int claimId);

}
