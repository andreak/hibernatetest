package no.officenet.test.hibernatetest.service;

import no.officenet.test.hibernatetest.infrastructure.jpa.AbstractGenericRepository;
import no.officenet.test.hibernatetest.model.AbstractEntity;
import org.springframework.stereotype.Repository;

@Repository
public class EntityRepositoryImpl extends AbstractGenericRepository<AbstractEntity, Long> implements EntityRepository {

	public EntityRepositoryImpl() {
		super(AbstractEntity.class);
	}

}
