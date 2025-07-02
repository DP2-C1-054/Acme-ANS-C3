<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "flight-crew-member.flight-assignment.list.label.scheduledDeparture" path= "scheduledDeparture"/>
	<acme:list-column code = "flight-crew-member.flight-assignment.list.label.flight-number" path= "flightNumber"/>
	<acme:list-column code = "flight-crew-member.flight-assignment.list.label.duty" path= "duty"/>
	<acme:list-column code = "flight-crew-member.flight-assignment.list.label.status" path= "status"/>
	<acme:list-column code = "flight-crew-member.flight-assignment.list.label.draftMode" path= "draftMode"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="flight-crew-member.flight-assignment.list.button.create" action="/flight-crew-member/flight-assignment/create"/>
<acme:button code="flight-crew-member.flight-assignment.list.button.list-activity-logs" action="/flight-crew-member/activity-log/list"/>

