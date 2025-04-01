<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airline-manager.leg.list.label.flightNumber" path="flightNumber" width="20%"/>
	<acme:list-column code="airline-manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="20%"/>
	<acme:list-column code="airline-manager.leg.list.label.airportDeparture" path="airportDeparture" width="20%"/>
	<acme:list-column code="airline-manager.leg.list.label.airportArrival" path="airportArrival" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="airline-manager.leg.list.button.create" action="/airline-manager/leg/create"/>
</jstl:if>	