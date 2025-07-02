
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.booking.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	List<String>				lastFiveDestinations;
	Double						moneySpentDuringLastYear;
	Map<TravelClass, Integer>	numberOfbookingsByTravelClass;
	Statistics					bookingFiveLastYears;
	Statistics					passengersInBookings;

}
