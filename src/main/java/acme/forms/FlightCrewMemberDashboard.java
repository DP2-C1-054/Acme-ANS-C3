
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.flight_assignments.AssignmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	protected static final long				serialVersionUID	= 1L;

	private List<String>					lastFiveDestinations;

	private Integer							legsWithIncidentSeverity03;

	private Integer							legsWithIncidentSeverity47;

	private Integer							legsWithIncidentSeverity810;

	private List<String>					lastLegCrewMembers;

	private Map<AssignmentStatus, Integer>	flightAssignmentsGroupedByStatus;

	private Statistics						flightAssignmentsStatistics;

}
