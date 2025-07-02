
package acme.features.airline_managers.dashboards;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.legs.Leg;
import acme.forms.airline_manager.Dashboard;
import acme.forms.airline_manager.FlightStatistics;
import acme.forms.airline_manager.LegsByStatus;
import acme.realms.airline_managers.AirlineManager;

@GuiService
public class AirlineManagerDashboardShowService extends AbstractGuiService<AirlineManager, Dashboard> {

	@Autowired
	private AirlineManagerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Dashboard dashboard;
		Integer rankingManagerByExperience;
		Integer yearsToRetire;
		Double ratioOnTimeLegs;
		Double ratioDelayedLegs;
		Airport mostPopular;
		Airport lessPopular;
		List<LegsByStatus> numberLegsByStatus;
		FlightStatistics statisticsAboutFlights;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		rankingManagerByExperience = this.repository.findRankingManagerByExperience(managerId);
		Date currentDate = MomentHelper.getCurrentMoment();
		yearsToRetire = this.repository.findYearsUntilRetirement(managerId, currentDate);
		ratioOnTimeLegs = this.repository.findRatioStatusLegs(managerId, Leg.Status.ON_TIME);
		ratioDelayedLegs = this.repository.findRatioStatusLegs(managerId, Leg.Status.DELAYED);
		mostPopular = this.repository.findMostPopularAirport(managerId, PageRequest.of(0, 1)).stream().findFirst().orElse(null);
		lessPopular = this.repository.findLessPopularAirport(managerId, PageRequest.of(0, 1)).stream().findFirst().orElse(null);
		numberLegsByStatus = this.repository.findNumberOfLegsByStatus(managerId);
		statisticsAboutFlights = this.repository.findStatisticsFromMyFlights(managerId);

		dashboard = new Dashboard();
		dashboard.setRankingManagerByExperience(rankingManagerByExperience);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setRatioOnTimeLegs(ratioOnTimeLegs);
		dashboard.setRatioDelayedLegs(ratioDelayedLegs);
		dashboard.setMostPopular(mostPopular);
		dashboard.setLessPopular(lessPopular);
		dashboard.setNumberLegsByStatus(numberLegsByStatus);
		dashboard.setStatisticsAboutFlights(statisticsAboutFlights);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final Dashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, "rankingManagerByExperience", "yearsToRetire", "ratioOnTimeLegs", "ratioDelayedLegs");
		dataset.put("mostPopular", dashboard.getMostPopular() == null ? null : dashboard.getMostPopular().getName());
		dataset.put("lessPopular", dashboard.getLessPopular() == null ? null : dashboard.getLessPopular().getName());

		Map<Leg.Status, Integer> statuses = AirlineManagerDashboardShowService.numberLegsByStatus(dashboard.getNumberLegsByStatus());
		dataset.put("numberOfOnTime", statuses.get(Leg.Status.ON_TIME));
		dataset.put("numberOfDelayed", statuses.get(Leg.Status.DELAYED));
		dataset.put("numberOfCancelled", statuses.get(Leg.Status.CANCELLED));
		dataset.put("numberOfLanded", statuses.get(Leg.Status.LANDED));

		dataset.put("costAverageFlights", dashboard.getStatisticsAboutFlights().getCostAverage());
		dataset.put("costMinFlights", dashboard.getStatisticsAboutFlights().getMinimum());
		dataset.put("costMaxFlights", dashboard.getStatisticsAboutFlights().getMaximum());
		dataset.put("costDeviationFlights", dashboard.getStatisticsAboutFlights().getStandardDeviation());

		super.getResponse().addData(dataset);
	}

	private static Map<Leg.Status, Integer> numberLegsByStatus(final List<LegsByStatus> ls) {
		Map<Leg.Status, Integer> res = new HashMap<>();
		Leg.Status[] statuses = Leg.Status.values();
		for (Leg.Status status : statuses)
			res.put(status, 0);
		for (LegsByStatus legs : ls)
			res.put(legs.getStatus(), legs.getLegsNumber());
		return res;
	}
}
