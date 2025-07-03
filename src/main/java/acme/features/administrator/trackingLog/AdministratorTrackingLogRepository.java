
package acme.features.administrator.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.tracking_logs.TrackingLog;

@Repository
public interface AdministratorTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.draftMode = false")
	Collection<TrackingLog> findAllTrackingLogsByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = :trackingLogId")
	TrackingLog findTrackingLogById(int trackingLogId);
}
