<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airline-manager.leg.form.label.flightNumber" path="flightNumber"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-textbox code="airline-manager.leg.form.label.status" path="status" />
	<acme:input-textbox code="airline-manager.leg.form.label.aircraft" path="aircraft" />
	<acme:input-textbox code="airline-manager.leg.form.label.airportDeparture" path="departureAirport" />
	<acme:input-textbox code="airline-manager.leg.form.label.airportArrival" path="arrivalAirport" />
	<acme:input-textbox code="airline-manager.leg.form.label.duration" path="duration" />
	<input type="hidden" name="flightId" value="${flightId}" />
</acme:form>