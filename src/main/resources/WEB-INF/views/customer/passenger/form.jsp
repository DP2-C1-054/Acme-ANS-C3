<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.passenger.form.label.name" path="name"/>
	<acme:input-textbox placeholder="acme.passenger.form.mail.placeholder" code="customer.passenger.form.label.mail" path="mail"/>
	<acme:input-textbox placeholder="acme.passenger.form.passport.placeholder" code="customer.passenger.form.label.passport" path="passport"/>
	<acme:input-moment code="customer.passenger.form.label.birthDate" path="birthDate"/>
	<acme:input-textarea code="customer.passenger.form.label.specialNeeds" path="specialNeeds"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')&& draftMode == true}">
			<acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
						<acme:submit code="customer.passenger.form.button.delete" action="/customer/passenger/delete"/>
			<acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create?bookingId=${bookingId}"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>