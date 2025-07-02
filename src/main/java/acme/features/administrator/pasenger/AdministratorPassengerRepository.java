
package acme.features.administrator.pasenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.Takes;

@Repository
public interface AdministratorPassengerRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id= :id")
	Booking findBookingById(int id);

	@Query("select t.passenger from Takes t where t.booking.id= :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengerById(int id);

	@Query("select p from Passenger p where p.customer.id= :customerId")
	Collection<Passenger> findPassengersByCustomerId(int customerId);

	@Query("select t from Takes t where t.passenger.id = :passengerId and t.booking.draftMode = false")
	Collection<Takes> findTakesOfPassengerWithPublishedBooking(int passengerId);

}
