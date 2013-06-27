package no.officenet.test.hibernatetest.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

	public Car(String model) {
		this.model = model;
	}

	@Column(name = "model")
	private String model = null;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "data_id")
	private FileRawData data;

	public FileRawData getData() {
		return data;
	}

	public void setData(FileRawData data) {
		this.data = data;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
