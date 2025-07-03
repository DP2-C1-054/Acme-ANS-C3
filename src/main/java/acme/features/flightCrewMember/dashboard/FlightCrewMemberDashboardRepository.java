
package acme.features.flightCrewMember.dashboard;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight_assignments.AssignmentStatus;
import acme.entities.flight_assignments.FlightAssignment;

@Repository
public interface FlightCrewMemberDashboardRepository extends AbstractRepository {

	@Query("SELECT l.arrivalAirport.city FROM FlightAssignment fa JOIN fa.leg l WHERE fa.allocatedFlightCrewMember.id = :flightCrewMemberId ORDER BY fa.moment DESC")
	List<String> findLastFiveDestinations(int flightCrewMemberId, Pageable pageable);

	@Query("""
		SELECT COUNT(DISTINCT al.flightAssignment.leg)
		FROM ActivityLog al
		WHERE al.severityLevel BETWEEN :inValue AND :outValue
		AND al.flightAssignment.allocatedFlightCrewMember.id = :flightCrewMemberId
		""")
	Integer legsWithSeverity(int inValue, int outValue, int flightCrewMemberId);

	@Query("SELECT fa FROM FlightAssignment fa JOIN fa.leg l WHERE fa.allocatedFlightCrewMember.id = :flightCrewMemberId ORDER BY l.scheduledArrival DESC")
	List<FlightAssignment> findFlightAssignment(int flightCrewMemberId, Pageable pageable);

	@Query("SELECT DISTINCT fa.allocatedFlightCrewMember.userAccount.username FROM FlightAssignment fa WHERE fa.leg.id = :legId")
	List<String> memberLastLeg(int legId);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.allocatedFlightCrewMember.id = :flightCrewMemberId AND fa.status = :status")
	int nFaByStatus(int flightCrewMemberId, AssignmentStatus status);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.allocatedFlightCrewMember.id = :flightCrewMemberId AND EXTRACT(YEAR FROM fa.leg.scheduledArrival) = :year AND EXTRACT(MONTH FROM fa.leg.scheduledArrival) = :month")
	Integer faInMonth(int flightCrewMemberId, int year, int month);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE YEAR(fa.leg.scheduledArrival) = :moment AND fa.allocatedFlightCrewMember.id = :crewMemberId")
	Integer faLastYear(int moment, int crewMemberId);

}
