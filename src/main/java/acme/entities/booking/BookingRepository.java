
package acme.entities.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b")
	List<Booking> findAllBookings();

	@Query("Select COUNT(t) FROM Takes t WHERE t.booking.id = :bookingId")
	Integer findNumberOfPassengersByBookingId(int bookingId);

	@Query("Select t.passenger FROM Takes t WHERE t.booking.id = :bookingId")
	List<Passenger> findAllPassengerByBookingId(int bookingId);
}
