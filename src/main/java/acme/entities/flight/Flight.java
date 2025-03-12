
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.airport.Airport;
import acme.entities.legs.Leg;
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
	@ValidMoney(min = 0)
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	@OneToMany
	@Valid
	@Automapped
	private List<Leg>			legs;


	public Date getScheduledDeparture() {
		FlightRepository repo = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repo.getLegsByFlightId(this.getId());
		return legs.isEmpty() ? null : legs.get(0).getScheduledDeparture();
	}

	public Date getScheduledArrival() {
		FlightRepository repo = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repo.getLegsByFlightId(this.getId());
		return legs.isEmpty() ? null : legs.get(legs.size() - 1).getScheduledArrival();
	}

	public Airport getOriginCity() {
		FlightRepository repo = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repo.getLegsByFlightId(this.getId());
		return legs.isEmpty() ? null : legs.get(0).getDepartureAirport();
	}

	public Airport getDestinationCity() {
		FlightRepository repo = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repo.getLegsByFlightId(this.getId());
		return legs.isEmpty() ? null : legs.get(legs.size() - 1).getArrivalAirport();
	}

	public Integer getLayovers() {
		FlightRepository repo = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repo.getLegsByFlightId(this.getId());
		return Math.max(0, legs.size() - 1);
	}
}
