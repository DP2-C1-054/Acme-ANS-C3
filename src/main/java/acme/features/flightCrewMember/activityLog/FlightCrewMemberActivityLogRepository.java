
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activity_logs.ActivityLog;
import acme.entities.flight_assignments.FlightAssignment;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM ActivityLog al")
	Collection<ActivityLog> findAllActivityLogs();

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment.allocatedFlightCrewMember.id = :id")
	Collection<ActivityLog> findActivityLogsByCrewMemberId(Integer id);

	@Query("SELECT al FROM ActivityLog al WHERE al.id = :id")
	Optional<ActivityLog> findActivityLogById(Integer id);

	@Query("SELECT al FROM ActivityLog al WHERE al.id = :activityLogId AND al.flightAssignment.allocatedFlightCrewMember.id = :flightCrewMemberId")
	Optional<ActivityLog> findByIdAndFlightCrewMemberId(Integer activityLogId, Integer flightCrewMemberId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.allocatedFlightCrewMember.id = :id AND fa.draftMode = false AND fa.status = 'CONFIRMED'")
	Collection<FlightAssignment> findConfirmedFlightAssignmentsByCrewMemberId(Integer id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	Optional<FlightAssignment> findFlightAssignmentById(Integer id);
}
