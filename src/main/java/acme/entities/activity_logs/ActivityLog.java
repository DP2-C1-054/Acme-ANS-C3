
package acme.entities.activity_logs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.flight_assignments.FlightAssignment;
import acme.entities.flight_crew_members.FlightCrewMember;
import acme.entities.legs.Leg;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@Column(unique = true)
	private FlightCrewMember	logger;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				incidentType;

	@Mandatory
	@ValidString
	@Automapped
	private String				incidentDescription;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				severityLevel;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;
}
