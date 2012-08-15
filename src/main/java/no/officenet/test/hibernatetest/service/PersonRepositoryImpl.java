package no.officenet.test.hibernatetest.service;

import no.officenet.test.hibernatetest.infrastructure.jpa.AbstractGenericRepository;
import no.officenet.test.hibernatetest.model.Person;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryImpl extends AbstractGenericRepository<Person, Long> implements PersonRepository {

	public PersonRepositoryImpl() {
		super(Person.class);
	}
}
