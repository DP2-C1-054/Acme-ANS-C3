<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox placeholder = "acme.booking.form.textbox.placeholder" code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}"/>	
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
	<acme:input-integer placeholder = "acme.booking.form.integer.placeholder" code="customer.booking.form.label.creditCardNibble" path="creditCardNibble" />	
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish')}">
		<acme:input-money readonly="true" code ="customer.booking.form.label.price" path="price"/>
	</jstl:if>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == false}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?bookingId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/bookingList?bookingId=${id}"/>
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete"/>
			<jstl:if test="${canPublish == true}">
				<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
			</jstl:if>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create?bookingId=${id}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>