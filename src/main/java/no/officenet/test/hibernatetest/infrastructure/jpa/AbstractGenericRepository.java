package no.officenet.test.hibernatetest.infrastructure.jpa;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * Default implementation of {@link GenericRepository}
 * This uses Hibernate for all persistence-operations.
 * @param <T> The type of the persistent entity (ie. User)
 * @param <PK> The type of the primary key (ie. String)
 */
@Repository
abstract public class AbstractGenericRepository<T, PK extends Serializable> implements GenericRepository<T, PK> {

	protected Class<T> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

	public AbstractGenericRepository(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T retrieve(PK id) throws EntityNotFoundException {
		@SuppressWarnings("unchecked")
		T entity = (T) entityManager.find(entityClass, id);
		if (entity == null) {
			throw new EntityNotFoundException("Unable to retrieve " + entityClass.getSimpleName() + " with id='" + id + "'");
		}
		return entity;
	}

	@Override
	public List<T> listAll() throws UnsupportedOperationException {
		CriteriaQuery<T> criteria = entityManager.getCriteriaBuilder().createQuery(entityClass);
		criteria.from(entityClass);
		List<T> all = entityManager.createQuery(criteria).getResultList();
		return all;
	}

	@Override
	public T save(T entity) {
		@SuppressWarnings("unchecked")
		T saved = entityManager.merge(entity);
		return saved;
	}

	@Override
	public void remove(T entity) {
		@SuppressWarnings("unchecked")
		T oldVersion = entityManager.merge(entity);
		entityManager.remove(oldVersion);
	}

	@Override
	public void remove(PK id) {
		entityManager.remove(entityManager.getReference(entityClass, id));
	}
}
