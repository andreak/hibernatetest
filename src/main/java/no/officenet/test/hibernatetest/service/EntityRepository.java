package no.officenet.test.hibernatetest.service;


import no.officenet.test.hibernatetest.infrastructure.jpa.GenericRepository;
import no.officenet.test.hibernatetest.model.AbstractEntity;

public interface EntityRepository extends GenericRepository<AbstractEntity, Long> {
}
