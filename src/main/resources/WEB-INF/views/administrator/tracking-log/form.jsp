<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.trackingLog.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
	<acme:input-textbox code="administrator.trackingLog.form.label.step" path="step"/>	
	<acme:input-double code="administrator.trackingLog.form.label.percentage" path="percentage"/>	
	<acme:input-select code="administrator.trackingLog.form.label.status" path="status" choices="${statusOptions}"/>
	<acme:input-textbox code="administrator.trackingLog.form.label.resolution" path="resolution"/>
</acme:form>