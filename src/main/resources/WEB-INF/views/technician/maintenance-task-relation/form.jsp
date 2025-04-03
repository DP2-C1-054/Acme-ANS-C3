<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	<acme:input-select code="technician.mtr.form.task" path="task" choices="${tasks}"/>
	<acme:submit code="technician.mtr.form.button.create" action="/technician/maintenance-task-relation/create?maintenanceRecordId=${maintenanceRecordId}" />
</acme:form>