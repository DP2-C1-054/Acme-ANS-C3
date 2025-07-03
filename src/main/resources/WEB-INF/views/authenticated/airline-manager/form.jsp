<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
	<acme:input-textbox placeholder="acme.airline-manager.form.identifierNumber.placeholder" code="authenticated.airline-manager.form.label.identifierNumber" path="identifierNumber"/>
	<acme:input-textbox placeholder="acme.airline-manager.form.experience.placeholder" code="authenticated.airline-manager.form.label.experience" path="experience"/>
	<acme:input-moment placeholder="acme.airline-manager.form.birthdate.placeholder" code="authenticated.airline-manager.form.label.birthdate" path="birthdate"/>
	<acme:input-select code="authenticated.airline-manager.form.label.airline" path="airline" choices="${airlines}"/>
	<acme:input-textbox placeholder="acme.airline-manager.form.linkPicture.placeholder" code="authenticated.airline-manager.form.label.linkPicture" path="linkPicture"/>
	
	<jstl:if test="${_command == 'create'}"> 
		<acme:submit  code="authenticated.consumer.form.button.create" action="/authenticated/airline-manager/create"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.consumer.form.button.update" action="/authenticated/airline-manager/update"/>
	</jstl:if>
</acme:form>