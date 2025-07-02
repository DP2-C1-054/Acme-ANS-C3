<%@page%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

	<h2>
	<acme:print code="customer.customer-dashboard.form.title.general.indicators"/>
	</h2>
    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.booking.statistics" />
    </h3>
    <table class="table table-sm">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.count.booking" /></th>
            <td><acme:print value="${bookingFiveLastYears.count}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.average.booking" /></th>
            <td><acme:print value="${bookingFiveLastYears.average}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.minimum.booking" /></th>
            <td><acme:print value="${bookingFiveLastYears.minimum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.maximum.booking" /></th>
            <td><acme:print value="${bookingFiveLastYears.maximum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.devv.booking" /></th>
            <td><acme:print value="${bookingFiveLastYears.deviation}" /></td>
        </tr>
    </table>

    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.passenger.statistics" />
    </h3>
    <table class="table table-sm">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.count.passenger" /></th>
            <td><acme:print value="${passengersInBookings.count}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.average.passenger" /></th>
            <td><acme:print value="${passengersInBookings.average}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.minimum.passenger" /></th>
            <td><acme:print value="${passengersInBookings.minimum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.maximum.passenger" /></th>
            <td><acme:print value="${passengersInBookings.maximum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.devv.passenger" /></th>
            <td><acme:print value="${passengersInBookings.deviation}" /></td>
        </tr>
    </table>

    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.last-five-destinations" />
    </h3>
    <table class="table table-sm">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.last-five-destinations" /></th>
            <td><acme:print value="${theLastFiveDestinations}" /></td>
        </tr>
    </table>

    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.money-spent-last-year" />
    </h3>
    <table class="table table-sm">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.money-spent" /></th>
            <td><acme:print value="${moneySpentDuringLastYear}" /></td>
        </tr>
    </table>

    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.numBYTravelClass" />
    </h3>
    <table class="table table-sm">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.totalNumTravelClassEconomy" /></th>
            <td><acme:print value="${totalNumTravelClassEconomy}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.totalNumTravelClassBusiness" /></th>
            <td><acme:print value="${totalNumTravelClassBusiness}" /></td>
        </tr>
    </table>
    
    <acme:return />
