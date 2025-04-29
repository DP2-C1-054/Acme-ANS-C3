
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.constraints.ValidLocatorCode;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidLocatorCode
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@ValidNumber(min = 0, max = 9999)
	@Automapped
	private Integer				creditCardNibble;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Automapped
	private boolean				draftMode;


	@Transient
	public Money price() {
		BookingRepository repository;
		Integer passengersNumber;

		repository = SpringHelper.getBean(BookingRepository.class);
		passengersNumber = repository.findNumberOfPassengersByBookingId(this.getId());

		Money ticketCost = this.flight.getCost();

		Money price = new Money();

		price.setCurrency(ticketCost.getCurrency());
		price.setAmount(ticketCost.getAmount() * passengersNumber);

		return price;

	}

}
