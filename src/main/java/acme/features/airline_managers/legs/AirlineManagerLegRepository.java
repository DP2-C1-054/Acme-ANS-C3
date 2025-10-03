
package acme.features.airline_managers.legs;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activity_logs.ActivityLog;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.claims.Claim;
import acme.entities.flight.Flight;
import acme.entities.flight_assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.entities.tracking_logs.TrackingLog;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.id = :id ")
	public Leg findLegByLegId(int id);

	@Query("select a from Aircraft a")
	public List<Aircraft> findAllAircrafts();

	@Query("select l from Leg l where l.id = :id ")
	public Optional<Leg> findByLegId(int id);

	@Query("select l from Leg l where l.flight.manager.id = :id ")
	public List<Leg> findManagerLegsByManagerId(int id);

	@Query("select f from Flight f where f.id = :id")
	public Flight findFlightByFlightId(int id);

	@Query("select a from Aircraft a where a.id = :id")
	public Aircraft findAircraftByAircraftId(int id);

	@Query("select a from Airport a where a.id = :id")
	public Airport findAirportByAirportId(int id);

	@Query("select f from FlightAssignment f where f.leg.id = :id")
	public List<FlightAssignment> findFlightAssignmentsByLegId(int id);

	@Query("select c from Claim c where c.leg.id = :id")
	public List<Claim> findClaimsByLegId(int id);

	@Query("select a from ActivityLog a where a.flightAssignment.id = :id")
	public List<ActivityLog> findActivityLogsByFlightAssignmentId(int id);

	@Query("select t from TrackingLog t where t.claim.id = :id")
	public List<TrackingLog> findTrackingLogByClaimId(int id);

	@Query("select a from Airport a")
	public List<Airport> findAllAirports();

	@Query("select a from Aircraft a where a.airline in (select m.airline from AirlineManager m where m.id = :id)")
	public List<Aircraft> findAllAircraftsByManagerId(int id);

	@Query("select l from Leg l where l.flight.id = :id")
	List<Leg> findAllLegsByFlightId(int id);

	Optional<Flight> findByIdAndManagerId(Integer flightId, Integer managerId);

}
