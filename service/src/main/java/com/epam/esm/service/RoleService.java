package com.epam.esm.service;

import com.epam.esm.entity.Role;

/**
 * Service interface for CRUD operations on Role entities.
 */
public interface RoleService {

    /**
     * Saves the given Role entity.
     *
     * @param role the Role entity to be saved
     * @return the saved Role entity
     */
    Role save(Role role);
}