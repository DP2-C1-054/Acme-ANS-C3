
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidFlightNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Legs extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidFlightNumber
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@ValidString
	@Automapped
	private String				departureAirport;

	@Mandatory
	@ValidString
	@Automapped
	private String				arrivalAirport;

	@Mandatory
	@Valid
	@Automapped
	private Double				duration;

	@Mandatory
	@Valid
	@Enumerated(EnumType.STRING)
	private Status				status;

	// Esto iria con el tipo aircraft, pero como todavia no esta implementado toca esperar y dejarlo asi provisionalmente
	@Mandatory
	@Valid
	@Automapped
	private String				aircraft;


	public enum Status {
		ON_TIME, DELAYED, CANCELLED, LANDED;
	}

}
