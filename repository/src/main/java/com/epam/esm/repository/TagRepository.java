package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * Repository interface for CRUD operations on Tag entities.
 */
public interface TagRepository extends BaseRepository<Tag> {
    /**
     * Retrieves a Tag by its name.
     *
     * @param name the name of the Tag to be retrieved
     * @return an {@code Optional} containing the Tag, or empty if not found
     */
    Optional<Tag> findByName(String name);

    /**
     * Retrieves the most used Tag of a User with the highest order cost.
     *
     * @param userId the ID of the user
     * @return the most used Tag of the user with the highest order cost
     */
    Tag findMostUsedTagOfUserWithHighestOrderCost(Long userId);
}
