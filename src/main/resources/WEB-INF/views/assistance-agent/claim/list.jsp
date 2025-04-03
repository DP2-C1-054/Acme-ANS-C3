<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgent.claim.list.label.passengerEmail" path="passengerEmail" width="20%"/>
	<acme:list-column code="assistanceAgent.claim.list.label.type" path="type" width="20%"/>
	<acme:list-column code="assistanceAgent.claim.list.label.indicator" path="indicator" width="20%"/>
	
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="assistanceAgent.claim.list.button.create" action="/assistance-agent/claim/create"/>