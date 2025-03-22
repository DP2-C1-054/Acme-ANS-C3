
package acme.entities.claims;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.tracking_logs.TrackingLog;

public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.lastUpdateMoment DESC")
	List<TrackingLog> findTrackingLogsOrderByMoment(@Param("claimId") int claimId);
}
