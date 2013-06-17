package no.officenet.test.hibernatetest.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("COMPANY")
public class Company extends AbstractEntity {

	public Company() {
	}

	public Company(String name) {
		this.name = name;
	}

	@Column(name = "name")
	private String name = null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.ALL)
	private List<Person> employees = new ArrayList<>();

	// Hibernate barfs on this mapping: https://hibernate.atlassian.net/browse/HHH-7814
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "recipient_id", nullable = false)
	List<EmailAddress> emailAddresses = new ArrayList<>();

	public Company addEmployee(Person person) {
		person.setCompany(this);
		employees.add(person);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Person> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Person> employees) {
		this.employees = employees;
	}
}
