<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<div style="margin: 20px;">
    <table border="1" style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.last-five-destinations" /></th>
            <td style="padding: 5px;"><acme:print value="${lastFiveDestinations}" /></td>
        </tr>
    </table>

    <table border="1" style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.fcm-assigned" /></th>
            <td style="padding: 5px;"><acme:print value="${lastLegCrewMembers}" /></td>
        </tr>
    </table>
    
    <table border="1" style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.grouped-by-status-confirmed" /></th>
            <td style="padding: 5px;"><acme:print value="${CONFIRMED}" /></td>
        </tr>
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.grouped-by-status-pending" /></th>
            <td style="padding: 5px;"><acme:print value="${PENDING}" /></td>
        </tr>	
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.grouped-by-status-cancelled" /></th>
            <td style="padding: 5px;"><acme:print value="${CANCELLED}" /></td>
        </tr>
    </table>
    
    <table border="1" style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.legs-with-low-severity" /></th>
            <td style="padding: 5px;"><acme:print value="${legsWithIncidentSeverity03}" /></td>
        </tr>
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.legs-with-mid-severity" /></th>
            <td style="padding: 5px;"><acme:print value="${legsWithIncidentSeverity47}" /></td>
        </tr>
        <tr>	
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.legs-with-high-severity" /></th>
            <td style="padding: 5px;"><acme:print value="${legsWithIncidentSeverity810}" /></td>
        </tr>
    </table>
    
    <table border="1" style="width: 100%; border-collapse: collapse;">
        <tr>
            <th style="padding: 5px;"><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.statistics" /></th>
            <td style="padding: 5px;"><acme:print value="${flightAssignmentsStatistics.average}" /></td>
            <td style="padding: 5px;"><acme:print value="${flightAssignmentsStatistics.minimum}" /></td>
            <td style="padding: 5px;"><acme:print value="${flightAssignmentsStatistics.maximum}" /></td>
            <td style="padding: 5px;"><acme:print value="${flightAssignmentsStatistics.deviation}" /></td>
        </tr>
    </table>
</div>
