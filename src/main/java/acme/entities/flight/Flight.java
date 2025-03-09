
package acme.entities.flight;

import javax.persistence.Entity;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@Min(0)
	@Automapped
	private Integer				cost;

	// Falta informacion debido a que viene de la clase Legs la cual esta en la siguiente tarea del Student 1

}
