<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.aircraft" path="aircraft" width="25%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="40%"/>
	<acme:list-column code="technician.maintenance-record.list.label.maintenanceMoment" path="maintenanceMoment" width="30%"/>
	<acme:list-column code="technician.maintenance-record.list.label.nextInspectionDue" path="nextInspectionDue" width="30%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>