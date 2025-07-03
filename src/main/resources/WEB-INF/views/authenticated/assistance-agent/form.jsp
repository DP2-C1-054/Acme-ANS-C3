<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.employeeCode" path="employeeCode"/>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.spokenLanguages" path="spokenLanguages"/>
	<acme:input-moment code="authenticated.assistanceAgent.form.label.moment" path="moment"/>
	<acme:input-textarea code="authenticated.assistanceAgent.form.label.bio" path="bio"/>
	<acme:input-money code="authenticated.assistanceAgent.form.label.salary" path="salary"/>
	<acme:input-url code="authenticated.assistanceAgent.form.label.photoUrl" path="photoUrl"/>
	<acme:input-select code="authenticated.assistanceAgent.form.label.airline" path="airline" choices="${airlines}"/>
	
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.assistanceAgent.form.button.create" action="/authenticated/assistance-agent/create"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.assistanceAgent.form.button.update" action="/authenticated/assistance-agent/update"/>
	</jstl:if>	
</acme:form>