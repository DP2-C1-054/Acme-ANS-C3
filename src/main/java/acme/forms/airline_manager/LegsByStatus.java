
package acme.forms.airline_manager;

import acme.entities.legs.Leg;

public interface LegsByStatus {

	Leg.Status getStatus();
	void setStatus(Leg.Status status);

	Integer getLegsNumber();
	void setLegsNumber(Integer legsNumber);
}
