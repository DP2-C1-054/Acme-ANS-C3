
package acme.features.administrator.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;

@Repository
public interface AdministratorClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE c.draftMode = false")
	Collection<Claim> findAllPublishedClaims();

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();
}
