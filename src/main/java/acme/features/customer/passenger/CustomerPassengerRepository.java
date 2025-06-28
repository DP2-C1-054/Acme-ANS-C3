
package acme.features.customer.passenger;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.Takes;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id= :id")
	Booking findBookingById(int id);

	@Query("select t.passenger from Takes t where t.booking.id= :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengerById(int id);

	@Query("select t from Takes t where t.passenger.id= :passengerId")
	List<Takes> findTakesByPassengerId(int passengerId);

	@Query("select t from Takes t where t.booking.id= :id")
	List<Takes> findTakesByBookingId(int id);

	@Query("select p from Passenger p where p.customer.id= :customerId")
	Collection<Passenger> findPassengersByCustomerId(int customerId);

}
