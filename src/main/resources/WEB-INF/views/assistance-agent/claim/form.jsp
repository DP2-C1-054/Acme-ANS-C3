<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>



<acme:form>
	<acme:input-moment code="assistanceAgent.claim.form.label.registrationMoment" path="registrationMoment"/>	
	<acme:input-email code="assistanceAgent.claim.form.label.passengerEmail" path="passengerEmail"/>	
	<acme:input-textarea code="assistanceAgent.claim.form.label.description" path="description"/>	
	<acme:input-select code="assistanceAgent.claim.form.label.type" path="type" choices="${types}"/>
	<acme:input-select code="assistanceAgent.claim.form.label.leg" path="leg" choices="${legs}"/>
	
	<acme:input-textbox code="assistanceAgent.claim.form.label.indicator" path="indicator" readonly="true"/>
		
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|publish|update') && draftMode == false}">
			<acme:button code="assistanceAgent.claim.form.button.trackingLogs" action="/assistance-agent/tracking-log/list?claimId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|publish|update') && draftMode == true}">
			<acme:button code="assistanceAgent.claim.form.button.trackingLogs" action="/assistance-agent/tracking-log/list?claimId=${id}"/>
			<acme:submit code="assistanceAgent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
			<acme:submit code="assistanceAgent.claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistanceAgent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistanceAgent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>