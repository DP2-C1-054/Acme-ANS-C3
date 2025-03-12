
package acme.entities.passenger;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.entities.booking.Booking;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Takes extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@Automapped
	private Passenger			passenger;

	@Mandatory
	@Valid
	@Automapped
	private Booking				booking;

}
