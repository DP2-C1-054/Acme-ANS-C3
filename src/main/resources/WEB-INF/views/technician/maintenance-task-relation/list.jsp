<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.mtr.list.label.maintenanceRecord-aircraft" path="maintenanceRecord.aircraft.registrationNumber" width="30%"/>	
	<acme:list-column code="technician.mtr.list.label.maintenanceRecord.maintenanceMoment" path="maintenanceRecord.maintenanceMoment" width="50%"/>
	<acme:list-column code="technician.mtr.list.label.task.type" path="task.type" width="50%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.mtr.list.button.create" action="/technician/maintenance-task-relation/create?maintenanceRecordId=${maintenanceRecordId}"/>
</jstl:if>