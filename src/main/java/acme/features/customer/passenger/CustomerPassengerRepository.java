
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id= :id")
	Booking findBookingById(int id);

	@Query("select t.passenger from Takes t where t.booking.id= :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengergById(int id);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengerById(int id);

}
