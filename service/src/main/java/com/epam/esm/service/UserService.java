package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for CRUD operations on User entities.
 */
public interface UserService {
    /**
     * Retrieves the most used Tag of a User with the highest order cost.
     *
     * @param userId the ID of the user
     * @return the most used Tag of the user with the highest order cost
     */
    List<Tag> findMostUsedTagOfUserWithHighestOrderCost(Long userId);

    /**
     * Retrieves a paginated list of Users.
     *
     * @param page the page number (0-based)
     * @param size the number of entities per page
     * @return a list of Users for the specified page
     */
    List<User> findAllWithPage(int page, int size);

    /**
     * Retrieves a User by its ID.
     *
     * @param id the ID of the User to be retrieved
     * @return an {@code Optional} containing the User, or empty if not found
     */
    Optional<User> findById(Long id);
}
