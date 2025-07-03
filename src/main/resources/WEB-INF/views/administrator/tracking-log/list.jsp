<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.trackingLog.list.label.percentage" path="percentage" width="20%"/>
	<acme:list-column code="administrator.trackingLog.list.label.status" path="status" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>
    