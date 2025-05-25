<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.registrationMoment" path= "registrationMoment"/>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.incidentType" path= "incidentType"/>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.incidentDescription" path= "incidentDescription"/>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.severityLevel" path= "severityLevel"/>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.flightAssignment" path= "flightAssignment"/>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.draftMode" path= "draftMode"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create"/>
