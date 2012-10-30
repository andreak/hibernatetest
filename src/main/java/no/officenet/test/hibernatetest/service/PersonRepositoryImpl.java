package no.officenet.test.hibernatetest.service;

import no.officenet.test.hibernatetest.infrastructure.jpa.AbstractGenericRepository;
import no.officenet.test.hibernatetest.model.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class PersonRepositoryImpl extends AbstractGenericRepository<Person, Long> implements PersonRepository {

	public PersonRepositoryImpl() {
		super(Person.class);
	}

	@Override
	public Person findByUserName(String userName) {
		try {
			return entityManager.createQuery("SELECT c FROM Person c WHERE c.userName = :userName", entityClass).
				setParameter("userName", userName).
				getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

}
