package com.epam.esm.service;

import java.util.List;
import java.util.Optional;

/**
 * Base service interface for CRUD operations on entities.
 *
 * @param <T> the type of entities managed by this service
 */
public interface BaseService<T> {
    /**
     * Creates the given entity.
     *
     * @param entity the entity to be created
     * @return the created entity
     */
    T create(T entity);

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     */
    List<T> findAll();

    /**
     * Retrieves a paginated list of entities.
     *
     * @param page the page number (0-based)
     * @param size the number of entities per page
     * @return a list of entities for the specified page
     */
    List<T> findAllWithPage(int page, int size);

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return an {@code Optional} containing the entity, or empty if not found
     */
    Optional<T> findById(Long id);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to be deleted
     */
    void delete(Long id);
}
