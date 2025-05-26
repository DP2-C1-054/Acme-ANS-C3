
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight_assignments.FlightAssignment;
import acme.entities.legs.Leg;

public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.status = 'LANDED' AND fa.allocatedFlightCrewMember.id = :id")
	List<FlightAssignment> findCompletedFlightAssignments(Integer id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.status != 'LANDED' AND fa.allocatedFlightCrewMember.id = :id")
	List<FlightAssignment> findPlannedFlightAssignments(Integer id);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT l FROM Leg l WHERE l.scheduledDeparture > date")
	Collection<Leg> findAllLegsAfterDate(Date date);

	@Query("SELECT l from Leg l WHERE l.id = :id")
	Optional<Leg> findLegById(Integer id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	Optional<FlightAssignment> findFlightAssignmentById(Integer id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id AND fa.allocatedFlightCrewMember.id = :flightCrewMemberId")
	Optional<FlightAssignment> findByIdAndFlightCrewMemberId(Integer id, Integer flightCrewMemberId);

}
