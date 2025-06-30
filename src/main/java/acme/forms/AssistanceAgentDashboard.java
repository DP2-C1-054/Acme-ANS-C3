
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						resolvedClaims;
	Double						rejectedClaims;
	List<String>				topThreeMonths;
	Statistics					logsStatistics;
	Statistics					assistedStatistics;

}
