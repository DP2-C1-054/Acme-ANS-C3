<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
		<acme:input-select code="customer.takes.form.label.passport" path="passenger" choices="${passengers}"/>

	
		<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.takes.form.button.create" action="/customer/takes/create?bookingId=${bookingId}"/>
		</jstl:when>		
		<jstl:when test="${_command == 'delete'}">
			<acme:submit code="customer.takes.form.button.delete" action="/customer/takes/delete?bookingId=${bookingId}"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>