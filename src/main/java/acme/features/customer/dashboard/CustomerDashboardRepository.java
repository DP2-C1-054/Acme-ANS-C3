
package acme.features.customer.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select c from Customer c where c.id=:id")
	Customer findCustomerById(int id);

	@Query("select f from Flight f where f.id=:id")
	Flight findFlightById(int id);

	@Query("select b.flight from Booking b where b.customer.id = :customerId and b.flight.draftMode = false")
	List<Flight> findDestinations(int customerId);

	@Query("SELECT SUM(b.flight.cost.amount*(SELECT COUNT(t) FROM Takes t WHERE t.booking.id = b.id)) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= :date")
	Double moneySpentDuringLastYear(int customerId, Date date);

	@Query("select b from Booking b where b.customer.id = :customerId")
	List<Booking> findAllBookingsByCustomer(int customerId);

	@Query("SELECT COUNT(t) FROM Takes t WHERE t.passenger.customer.id = :customerId")
	Integer countPassengersByCustomer(int customerId);

	@Query("SELECT AVG(select count(t) from Takes t where t.booking.id=b.id) FROM Booking b WHERE b.customer.id = :customerId")
	Double averagePassengersByCustomer(int customerId);

	@Query("SELECT MIN(select count(t) from Takes t where t.booking.id=b.id) FROM Booking b WHERE b.customer.id = :customerId")
	Double minPassengersByCustomer(int customerId);

	@Query("SELECT MAX(select count(t) from Takes t where t.booking.id=b.id) FROM Booking b WHERE b.customer.id = :customerId")
	Double maxPassengersByCustomer(int customerId);

	@Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= :dateLimit")
	Integer countBookingsInDate(int customerId, Date dateLimit);

	@Query("SELECT AVG(b.flight.cost.amount * (SELECT COUNT(t) FROM Takes t WHERE t.booking.id = b.id)) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= :dateLimit")
	Double averageBookingCostInDate(int customerId, Date dateLimit);

	@Query("SELECT MIN(b.flight.cost.amount * (SELECT COUNT(t) FROM Takes t WHERE t.booking.id = b.id)) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= :dateLimit")
	Double minBookingCostInDate(int customerId, Date dateLimit);

	@Query("SELECT MAX(b.flight.cost.amount * (SELECT COUNT(t) FROM Takes t WHERE t.booking.id = b.id)) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= :dateLimit")
	Double maxBookingCostInDate(int customerId, Date dateLimit);

}
