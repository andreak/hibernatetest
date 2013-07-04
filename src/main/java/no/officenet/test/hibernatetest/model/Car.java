package no.officenet.test.hibernatetest.model;

import no.officenet.test.hibernatetest.infrastructure.jpa.JodaDateTimeConverter;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "car")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("CAR")
public class Car extends AbstractEntity {

	public Car() {
	}

	public Car(DateTime created, String model) {
		this.model = model;
		this.created = created;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_name", referencedColumnName = "username") // Don't use PK to show non-lazy behavior
	private Person owner = null;

	@Column(name = "model", nullable = false)
	private String model = null;

	@Column(name = "created", nullable = false)
	private DateTime created = null;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "data_id")
	private FileRawData data;

	public FileRawData getData() {
		return data;
	}

	public void setData(FileRawData data) {
		this.data = data;
	}

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

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}
}
