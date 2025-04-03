<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.task.list.label.type" path="type" width="40%"/>
	<acme:list-column code="technician.task.list.label.priority" path="priority" width="30%"/>
	<acme:list-column code="technician.task.list.label.estimatedDuration" path="estimatedDuration" width="30%"/>
	<acme:list-payload path="payload"/>	
</acme:list>

<jstl:if test="${empty maintenanceRecordId}">
	<acme:button code="technician.task.form.button.create" action="/technician/task/create"/>
</jstl:if>

<jstl:if test="${showCreate}">
	<acme:button code="technician.task.form.button.create" action="/technician/task/create?maintenanceRecordId=${maintenanceRecordId}"/>
	<acme:button code="technician.task.form.button.add" action="/technician/maintenance-task-relation/create?maintenanceRecordId=${maintenanceRecordId}"/>
</jstl:if>