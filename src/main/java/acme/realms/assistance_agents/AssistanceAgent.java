
package acme.realms.assistance_agents;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAssistanceAgent;
import acme.constraints.ValidRoleIdentifier;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAssistanceAgent
public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidRoleIdentifier
	private String				employeeCode;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				spokenLanguages;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				bio;

	@Optional
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidUrl
	@Automapped
	private String				photoUrl;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
