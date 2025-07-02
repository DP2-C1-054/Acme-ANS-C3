
package acme.features.customer.dashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.forms.CustomerDashboard;
import acme.realms.customer.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	@Autowired
	private CustomerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}
	@Override
	public void load() {

		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		CustomerDashboard dashboard;
		int id = customer.getId();
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		LocalDate localDateMoment = moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dateLimitOneYear = localDateMoment.minusYears(1);

		LocalDate dateLimitFiveYear = localDateMoment.minusYears(5);

		Date dateLastYear = Date.from(dateLimitOneYear.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateLastFiveYears = Date.from(dateLimitFiveYear.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<String> theLastFiveDestinations;

		Double moneySpentInBookingDuringLastYear;

		Map<TravelClass, Integer> bookingsGroupedByTravelClass;

		Integer countPassenger;
		Double averagePassenger;
		Double minPassenger;
		Double maxPassenger;
		Double stddevPassenger;

		Integer countBooking;
		Double averageBooking;
		Double minBooking;
		Double maxBooking;
		Double stddevBooking;

		theLastFiveDestinations = this.findLast5Destinations(customer.getId());
		moneySpentInBookingDuringLastYear = this.repository.moneySpentDuringLastYear(id, dateLastYear);
		bookingsGroupedByTravelClass = this.groupByTravelClass(id);

		countPassenger = this.repository.countPassengersByCustomer(id) != null ? this.repository.countPassengersByCustomer(id) : 0;
		averagePassenger = this.repository.averagePassengersByCustomer(id) != null ? this.repository.averagePassengersByCustomer(id) : 0;
		minPassenger = this.repository.minPassengersByCustomer(id) != null ? this.repository.minPassengersByCustomer(id) : 0.0;
		maxPassenger = this.repository.maxPassengersByCustomer(id) != null ? this.repository.maxPassengersByCustomer(id) : 0.0;

		stddevPassenger = this.calculatePassengerDeviation(id) != null ? this.calculatePassengerDeviation(id) : 0.0;
		countBooking = this.repository.countBookingsInDate(id, dateLastFiveYears) != null ? this.repository.countBookingsInDate(id, dateLastFiveYears) : 0;
		averageBooking = this.repository.averageBookingCostInDate(id, dateLastFiveYears) != null ? this.repository.averageBookingCostInDate(id, dateLastFiveYears) : 0;
		minBooking = this.repository.minBookingCostInDate(id, dateLastFiveYears) != null ? this.repository.minBookingCostInDate(id, dateLastFiveYears) : 0.0;
		maxBooking = this.repository.maxBookingCostInDate(id, dateLastFiveYears) != null ? this.repository.maxBookingCostInDate(id, dateLastFiveYears) : 0.0;
		stddevBooking = this.calculateBookingDeviation(id, dateLastFiveYears) != null ? this.calculateBookingDeviation(id, dateLastFiveYears) : 0.0;

		Statistics statPassenger = new Statistics();
		statPassenger.setCount(countPassenger);
		statPassenger.setAverage(averagePassenger);
		statPassenger.setMaximum(maxPassenger);
		statPassenger.setMinimum(minPassenger);
		statPassenger.setDeviation(stddevPassenger);

		Statistics statBooking = new Statistics();
		statBooking.setCount(countBooking);
		statBooking.setAverage(averageBooking);
		statBooking.setMaximum(maxBooking);
		statBooking.setMinimum(minBooking);
		statBooking.setDeviation(stddevBooking);

		dashboard = new CustomerDashboard();
		dashboard.setLastFiveDestinations(theLastFiveDestinations);
		dashboard.setMoneySpentDuringLastYear(moneySpentInBookingDuringLastYear);
		dashboard.setNumberOfbookingsByTravelClass(bookingsGroupedByTravelClass);
		dashboard.setBookingFiveLastYears(statBooking);
		dashboard.setPassengersInBookings(statPassenger);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard customerDashboard) {
		Dataset dataset;
		Integer totalNumBUSINESS;
		Integer totalNumEconomy;
		String res5LastCity = String.join(", ", customerDashboard.getLastFiveDestinations());

		totalNumBUSINESS = customerDashboard.getNumberOfbookingsByTravelClass().get(TravelClass.BUSINESS) != null ? customerDashboard.getNumberOfbookingsByTravelClass().get(TravelClass.BUSINESS) : 0;
		totalNumEconomy = customerDashboard.getNumberOfbookingsByTravelClass().get(TravelClass.ECONOMY) != null ? customerDashboard.getNumberOfbookingsByTravelClass().get(TravelClass.ECONOMY) : 0;

		dataset = super.unbindObject(customerDashboard, //
			"moneySpentDuringLastYear",// 
			"bookingFiveLastYears", "passengersInBookings");
		dataset.put("totalNumTravelClassEconomy", totalNumEconomy);
		dataset.put("totalNumTravelClassBusiness", totalNumBUSINESS);
		dataset.put("theLastFiveDestinations", res5LastCity);

		super.getResponse().addData(dataset);
	}

	private List<String> findLast5Destinations(final int id) {
		List<Flight> destinations = this.repository.findDestinations(id);
		return destinations.stream().sorted(Comparator.comparing(Flight::getScheduledDeparture).reversed()).map(x -> x.getDestinationCity()).distinct().limit(5).collect(Collectors.toList());
	}

	private Map<TravelClass, Integer> groupByTravelClass(final int id) {
		List<Booking> allBookings = this.repository.findAllBookingsByCustomer(id);
		Map<TravelClass, Integer> res = new HashMap<TravelClass, Integer>();
		for (Booking b : allBookings)
			if (!res.containsKey(b.getTravelClass()))
				res.put(b.getTravelClass(), 1);
			else
				res.put(b.getTravelClass(), res.get(b.getTravelClass()) + 1);
		return res;
	}

	private Double calculatePassengerDeviation(final int id) {
		Integer totalPassengers = this.repository.countPassengersByCustomer(id);

		Double averagePassengers = this.repository.averagePassengersByCustomer(id);

		Double minPassengers = this.repository.minPassengersByCustomer(id);

		Double maxPassengers = this.repository.maxPassengersByCustomer(id);

		double variance = 0.0;
		if (averagePassengers != null && minPassengers != null && maxPassengers != null)
			for (int i = minPassengers.intValue(); i <= maxPassengers.intValue(); i++) {
				Double diff = i - averagePassengers;
				variance += Math.pow(diff, 2);
			}

		return Math.sqrt(variance / totalPassengers);

	}

	private Double calculateBookingDeviation(final int id, final Date dateLastFiveYears) {
		Integer totalBookingCost = this.repository.countBookingsInDate(id, dateLastFiveYears);

		Double averageBookingCost = this.repository.averageBookingCostInDate(id, dateLastFiveYears);

		Double minBookingCost = this.repository.minBookingCostInDate(id, dateLastFiveYears);

		Double maxBookingCost = this.repository.maxBookingCostInDate(id, dateLastFiveYears);

		double variance = 0.0;

		if (averageBookingCost != null && minBookingCost != null && maxBookingCost != null)
			for (int i = minBookingCost.intValue(); i <= maxBookingCost.intValue(); i++) {
				Double diff = i - averageBookingCost;
				variance += Math.pow(diff, 2);
			}

		return Math.sqrt(variance / totalBookingCost);

	}

}
