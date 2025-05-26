
package acme.entities.maintenance_task_relation;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.tasks.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = {
	@Index(columnList = "maintenance_record_id"), @Index(columnList = "task_id"), @Index(columnList = "task_id, maintenance_record_id")
})
public class MaintenanceTaskRelation extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Task				task;

}
