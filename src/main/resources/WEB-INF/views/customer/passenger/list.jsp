<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.name" path="name" width="90%"/>	
	<acme:list-column code="customer.passenger.list.label.passport" path="passport" width="10%"/>	
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create?bookingId=${bookingId}"/>
	<acme:button code="customer.passenger.list.button.add" action="/customer/takes/create?bookingId=${bookingId}"/>
</jstl:if>
<jstl:if test="${showDelete}">
	<acme:button code="customer.passenger.list.button.delete" action="/customer/takes/delete?bookingId=${bookingId}"/>
</jstl:if>