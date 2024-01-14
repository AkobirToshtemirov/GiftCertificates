package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * Repository interface for CRUD operations on User entities.
 */
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
