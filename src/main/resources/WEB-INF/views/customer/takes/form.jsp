<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
		<acme:input-select code="customer.takes.form.label.passport" path="passenger" choices="${passengers}"/>
	<acme:submit code="customer.takes.form.button.create" action="/customer/takes/create?bookingId=${bookingId}"/>
</acme:form>