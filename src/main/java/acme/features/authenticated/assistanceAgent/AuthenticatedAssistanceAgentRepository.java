
package acme.features.authenticated.assistanceAgent;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;
import acme.realms.assistance_agents.AssistanceAgent;

@Repository
public interface AuthenticatedAssistanceAgentRepository extends AbstractRepository {

	@Query("SELECT a FROM AssistanceAgent a where a.userAccount.id = :id")
	AssistanceAgent findAssistanceAgentByUserAccountId(int id);

	@Query("SELECT u FROM UserAccount u where u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("SELECT a FROM AssistanceAgent a where a.employeeCode = :code")
	AssistanceAgent findAssistanceAgentByEmployeeCode(String code);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> findAllAirlines();

	@Query("SELECT a FROM Airline a where a.id = :id")
	Airline findAirlineById(int id);

}
