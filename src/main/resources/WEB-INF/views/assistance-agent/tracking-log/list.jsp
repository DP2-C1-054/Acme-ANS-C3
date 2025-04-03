<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgent.trackingLog.list.label.percentage" path="percentage" width="20%"/>
	<acme:list-column code="assistanceAgent.trackingLog.list.label.status" path="status" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>
    
    <acme:button code="assistanceAgent.trackingLog.list.button.create" action="/assistance-agent/tracking-log/create?claimId=${param.claimId}"/>
