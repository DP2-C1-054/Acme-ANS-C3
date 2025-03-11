
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airport.Airport;
import acme.entities.legs.Legs;
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
	@Valid
	@Automapped
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Integer				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	@OneToMany()
	@Valid
	@Automapped
	private List<Legs>			legs;


	public Date getScheduledDeparture() {
		return this.legs != null && !this.legs.isEmpty() ? this.legs.get(0).getScheduledDeparture() : null;
	}

	public Date getScheduledArrival() {
		return this.legs != null && !this.legs.isEmpty() ? this.legs.get(this.legs.size() - 1).getScheduledArrival() : null;
	}

	public Airport getOriginCity() {
		return this.legs != null && !this.legs.isEmpty() ? this.legs.get(0).getDepartureAirport() : null;
	}

	public Airport getDestinationCity() {
		return this.legs != null && !this.legs.isEmpty() ? this.legs.get(this.legs.size() - 1).getArrivalAirport() : null;
	}

	public Integer getLayovers() {
		return this.legs != null && !this.legs.isEmpty() ? this.legs.size() : 0;
	}
}
