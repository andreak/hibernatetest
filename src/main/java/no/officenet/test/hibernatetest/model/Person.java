package no.officenet.test.hibernatetest.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("PERSON")
public class Person extends AbstractEntity{

	public Person() {
	}

	public Person(String userName, String firstName, String lastName, List<Car> cars) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cars = cars;
		for (Car car : cars) {
			car.setOwner(this);
		}
	}

	@Column(name = "username", nullable = false)
	private String userName = null;

	@Column(name = "firstname")
	private String firstName = null;

	@Column(name = "lastname")
	private String lastName = null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
	List<Car> cars = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "company_id")
	private Company company = null;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
