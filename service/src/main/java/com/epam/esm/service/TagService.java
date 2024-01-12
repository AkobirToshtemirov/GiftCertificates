package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * Service interface for CRUD operations on Tag entities.
 */
public interface TagService extends BaseService<Tag> {
    /**
     * Retrieves a Tag by its name.
     *
     * @param name the name of the Tag to be retrieved
     * @return an {@code Optional} containing the Tag, or empty if not found
     */
    Optional<Tag> findTagByName(String name);
}
