
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.Takes;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select t.passenger from Takes t where t.booking.id = ?1 ")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select f from Flight f where f.draftMode = false and (SELECT MIN(l.scheduledDeparture) FROM Leg l WHERE l.flight.id =f.id)> :currentMoment")
	Collection<Flight> findPublishedFlights(Date currentMoment);

	@Query("select t from Takes t where t.booking.id =?1 ")
	Collection<Takes> findTakesByBookingId(int bookingId);

}
