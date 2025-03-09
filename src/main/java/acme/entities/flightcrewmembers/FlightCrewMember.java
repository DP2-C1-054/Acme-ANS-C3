
package acme.entities.flightcrewmembers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractEntity {

	private static final long				serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Column(unique = true)
	private String							employeeCode;

	@Mandatory
	@ValidString(pattern = "^'\\+?\\d{6,15}$")
	@Column(unique = true)
	private String							phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String							languageSkills;

	@Mandatory
	@Valid
	@Automapped
	private Airline							airline;

	@Mandatory
	@Valid
	@Automapped
	private FlightCrewMemberAvailability	availability;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money							salary;

	@Optional
	@ValidNumber(integer = 3)
	@Automapped
	private Integer							experienceYears;
}
