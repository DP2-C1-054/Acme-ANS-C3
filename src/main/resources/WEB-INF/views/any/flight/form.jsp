<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airline-manager.flight.form.label.tag" path="tag"/>
	<acme:input-money code="airline-manager.flight.form.label.cost" path="cost"/>
	<acme:input-textarea code="airline-manager.flight.form.label.description" path="description"/>
	<acme:input-checkbox code="airline-manager.flight.form.label.selfTransfer" path="requiresSelfTransfer"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="airline-manager.flight.form.button.legs" action="/any/leg/list?flightId=${id}" />
		</jstl:when>	
	</jstl:choose>
</acme:form>