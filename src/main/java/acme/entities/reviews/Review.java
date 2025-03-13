
package acme.entities.reviews;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidReview;
import acme.datatypes.UserIdentity;
import acme.entities.airlines.Airline;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.service.Service;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidReview
public class Review extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				subject;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				text;

	@Optional
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Double				score;

	@Optional
	@Valid
	@Automapped
	private Boolean				isRecommended;

	@Mandatory
	@Valid
	@Automapped
	private UserIdentity		reviewer;

	@Optional
	@Valid
	@ManyToOne(optional = true)
	private Airline				airlineReviewed;

	@Optional
	@Valid
	@ManyToOne(optional = true)
	private Service				serviceReviewed;

	@Optional
	@Valid
	@ManyToOne(optional = true)
	private Airport				airportReviewed;

	@Optional
	@Valid
	@ManyToOne(optional = true)
	private Flight				flightReviewed;

}
