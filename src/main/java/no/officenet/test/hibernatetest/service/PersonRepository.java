package no.officenet.test.hibernatetest.service;


import no.officenet.test.hibernatetest.infrastructure.jpa.GenericRepository;
import no.officenet.test.hibernatetest.model.Person;

public interface PersonRepository extends GenericRepository<Person, Long> {
}
