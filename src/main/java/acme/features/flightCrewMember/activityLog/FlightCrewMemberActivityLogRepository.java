
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activity_logs.ActivityLog;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM ActivityLog al")
	Collection<ActivityLog> findAllActivityLogs();

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment.allocatedFlightCrewMember.id = :id")
	Collection<ActivityLog> findActivityLogsByCrewMemberId(Integer id);

	@Query("SELECT al FROM ActivityLog al WHERE al.id = :id")
	Optional<ActivityLog> findActivityLogById(Integer id);
}
