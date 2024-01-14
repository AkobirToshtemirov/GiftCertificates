package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * Repository interface for CRUD operations on User entities.
 */
public interface UserRepository extends BaseRepository<User> {

    /**
     * Retrieves a User by their username.
     *
     * @param username the username of the User to be retrieved
     * @return an {@code Optional} containing the User, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a User by their email address.
     *
     * @param email the email address of the User to be retrieved
     * @return an {@code Optional} containing the User, or empty if not found
     */
    Optional<User> findByEmail(String email);
}