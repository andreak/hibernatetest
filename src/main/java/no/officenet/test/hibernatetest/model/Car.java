package no.officenet.test.hibernatetest.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "car")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("CAR")
public class Car extends AbstractEntity {

	public Car() {
	}

	public Car(String model) {
		this.model = model;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_name", referencedColumnName = "username") // Don't use PK to show non-lazy behavior
	private Person owner = null;

	@Column(name = "model")
	private String model = null;

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
