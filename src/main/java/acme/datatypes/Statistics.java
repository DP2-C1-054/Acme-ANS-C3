
package acme.datatypes;

import acme.client.components.basis.AbstractDatatype;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics extends AbstractDatatype {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Double				average;

	private Double				minimum;

	private Double				maximum;

	private Double				deviation;

	private Integer				count;

	// Object interface -------------------------------------------------------


	@Override
	public String toString() {
		return String.format("Statistics [average=%.2f, minimum=%.2f, maximum=%.2f, deviation=%.2f, count=%s]", this.average, this.minimum, this.maximum, this.deviation, this.count);
	}

	public Statistics() {

	}
}
