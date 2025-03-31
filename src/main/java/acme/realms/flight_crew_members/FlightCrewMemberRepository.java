
package acme.realms.flight_crew_members;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("SELECT f FROM FlightCrewMember f")
	List<FlightCrewMember> findAllFlightCrewMembers();

}
