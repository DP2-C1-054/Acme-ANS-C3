<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox readonly="true" placeholder = "acme.booking.form.textbox.placeholder" code="administrator.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-select readonly="true" code="administrator.booking.form.label.flight" path="flight" choices="${flights}"/>	
	<acme:input-select readonly="true" code="administrator.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
	<acme:input-integer readonly="true" placeholder = "acme.booking.form.integer.placeholder" code="administrator.booking.form.label.creditCardNibble" path="creditCardNibble" />	
	<jstl:if test="${_command == 'show'}">
		<acme:input-money readonly="true" code ="administrator.booking.form.label.price" path="price"/>
		<acme:button code="administrator.booking.form.button.passengers" action="/administrator/passenger/bookingList?bookingId=${id}"/>
	</jstl:if>
</acme:form>