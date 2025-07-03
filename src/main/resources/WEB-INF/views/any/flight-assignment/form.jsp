<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form >
	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.moment" path="moment" readonly="true"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices="${dutyChoices}" readonly="true"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.status" path="status" choices="${statusChoices}" readonly="true"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks" readonly="true"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.leg" path="leg" choices="${legChoices}" readonly="true" />
</acme:form>