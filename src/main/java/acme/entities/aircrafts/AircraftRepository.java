
package acme.entities.aircrafts;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("SELECT a FROM Aircraft a")
	List<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM Aircraft a WHERE a.id != :aircraftId AND a.registrationNumber = :registrationNumber")
	public List<Aircraft> findAllByRegistrationNumber(Integer aircraftId, String registrationNumber);

	@Query("SELECT COUNT(l) = 0 FROM Leg l WHERE l.aircraft.id = :aircraftId AND l.scheduledDeparture >= :date AND l.draftMode = false")
	public boolean isAircraftBusyInTheFuture(Integer aircraftId, Date date);

}
