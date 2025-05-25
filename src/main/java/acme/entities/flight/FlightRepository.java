
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;
import acme.entities.legs.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	List<Leg> getLegsByFlightId(int flightId);

	@Query("select l.scheduledDeparture from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	Date getScheduledDeparture(int flightId);

	@Query("select l.scheduledArrival from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	Date getScheduledArrival(int flightId);

	@Query("select l.departureAirport from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	Airport getOriginCity(int flightId);

	@Query("select l.arrivalAirport from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	Airport getDestinationCity(int flightId);

	@Query("select count(l) - 1 from Leg l where l.flight.id = :flightId")
	Integer getLayovers(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	List<Leg> legsDuringFlight(int flightId);
}
