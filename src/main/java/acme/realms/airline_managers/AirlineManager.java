
package acme.realms.airline_managers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAirlineManager;
import acme.constraints.ValidRoleIdentifier;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAirlineManager
public class AirlineManager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidRoleIdentifier
	@Column(unique = true)
	private String				identifierNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 50)
	@Automapped
	private Integer				experience;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				birthdate;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Optional
	@ValidUrl
	@Automapped
	private String				linkPicture;

}
