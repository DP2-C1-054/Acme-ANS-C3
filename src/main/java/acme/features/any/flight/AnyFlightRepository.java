
package acme.features.any.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.legs.Leg;

@Repository
public interface AnyFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false")
	Collection<Flight> findAllPublishedFlights();

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	List<Leg> findAllLegsByFlightId(int flightId);

}
