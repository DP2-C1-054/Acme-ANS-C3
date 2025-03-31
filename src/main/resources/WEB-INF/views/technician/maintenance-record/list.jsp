<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenanceRecord.list.label.moment" path="moment" width="80%"/>	
	<acme:list-column code="technician.maintenanceRecord.list.label.status" path="status" width="20%"/>
	<acme:list-column code="technician.maintenanceRecord.list.label.inspectionDueDate" path="inspectionDueDate" width="20%"/>
	<acme:list-column code="technician.maintenanceRecord.list.label.estimatedCost" path="estimatedCost" width="20%"/>
</acme:list>