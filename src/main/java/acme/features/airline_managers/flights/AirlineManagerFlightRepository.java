
package acme.features.airline_managers.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f")
	public List<Flight> findAllFlights();

	@Query("select f from Flight f where f.manager.id = :managerId")
	public List<Flight> findManagerFlightsByManagerId(int managerId);

	@Query("select f from Flight f where f.id = :id")
	public Flight findFlightById(int id);

}
