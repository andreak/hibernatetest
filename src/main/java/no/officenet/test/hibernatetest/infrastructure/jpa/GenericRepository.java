package no.officenet.test.hibernatetest.infrastructure.jpa;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;

/**
 * Interface for a generic-repository implementation.
 * This exposes common CRUD operations.
 * Note; Repositories which do not want to expose listAll() should throw {@link UnsupportedOperationException}.
 * @param <T> The type of the persistent entity (ie. User)
 * @param <PK> The type of the primary key (ie. String)
 */
public interface GenericRepository<T, PK extends Serializable> {

	/**
	 * Retrieves an entity by its primary key
	 * @param id The primary key
	 * @return The entity, never null
	 * @throws EntityNotFoundException when not found
	 */
	T retrieve(PK id) throws EntityNotFoundException;

	/**
	 * List all entities for this repository.
	 * This is an optional operation.
	 * @return A list of entities, empty list if none found.
	 * @throws UnsupportedOperationException If the implementation does not support listing all.
	 */
	List<T> listAll() throws UnsupportedOperationException;

	/**
	 * Save the entity.
	 * Call this operation for create and update. Let domain-service expose different create and update methods
	 * if it wants to treat them differently (for audit, validation etc.).
	 * @param entity The entity to save
	 * @return a new and updated instance of the entity.
	 */
	T save(T entity);

	/**
	 * Remove the entity
	 * @param entity The entity to remove
	 */
	void remove(T entity);

	/**
	 * Remove the entity by primary key
	 * @param id the primary key of the entity to remove
	 */
	void remove(PK id);

}
