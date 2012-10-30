package no.officenet.test.hibernatetest.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
@Table(name = "abstract_entity")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "abstract_entity_id_seq", allocationSize = 1)
public abstract class AbstractEntity implements Serializable {

	@Version
	private long version = 0;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_STORE")
	private Long id = null;

	public Long getId() {
		return id;
	}
}
