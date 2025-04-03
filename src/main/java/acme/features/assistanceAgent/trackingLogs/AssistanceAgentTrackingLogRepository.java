
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.tracking_logs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findAllTrackingLogsByClaimId(int claimId);
}
