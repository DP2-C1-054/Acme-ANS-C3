
package acme.features.airline_managers.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.legs.Leg;
import acme.entities.passenger.Takes;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f")
	public List<Flight> findAllFlights();

	@Query("select f from Flight f where f.manager.id = :managerId")
	public List<Flight> findManagerFlightsByManagerId(int managerId);

	@Query("select f from Flight f where f.id = :id")
	public Flight findFlightById(int id);

	@Query("select l from Leg	 l where l.flight.id = :flightId")
	public List<Leg> findLegsByFlightId(int flightId);

	@Query("select b from Booking b where b.flight.id = :flightId")
	public List<Booking> findBookingsByFlightId(int flightId);

	@Query("select t from Takes t where t.booking.flight.id = :flightId")
	public List<Takes> findTakesByFlightId(int flightId);

	@Query("select f from Flight f where f.manager.id = :managerId")
	public List<Flight> findFlightsByManagerId(int managerId);

}
