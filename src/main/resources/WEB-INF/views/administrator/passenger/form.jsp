<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.passenger.form.label.name" path="name"/>
	<acme:input-textbox placeholder="acme.passenger.form.mail.placeholder" code="administrator.passenger.form.label.mail" path="mail"/>
	<acme:input-textbox placeholder="acme.passenger.form.passport.placeholder" code="administrator.passenger.form.label.passport" path="passport"/>
	<acme:input-moment code="administrator.passenger.form.label.birthDate" path="birthDate"/>
	<acme:input-textarea code="administrator.passenger.form.label.specialNeeds" path="specialNeeds"/>
</acme:form>