package com.epam.esm.repository;

import com.epam.esm.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations on Role entities.
 */
public interface RoleRepository {

    /**
     * Saves the given Role entity.
     *
     * @param role the Role entity to be saved
     * @return the saved Role entity
     */
    Role save(Role role);

    /**
     * Retrieves a Role by its code.
     *
     * @param code the code of the Role to be retrieved
     * @return an {@code Optional} containing the Role, or empty if not found
     */
    Optional<Role> findByCode(String code);

    /**
     * Retrieves a list of Roles by user ID.
     *
     * @param userId the ID of the user
     * @return a list of Roles for the specified user ID
     */
    List<Role> findAllByUserId(Long userId);
}