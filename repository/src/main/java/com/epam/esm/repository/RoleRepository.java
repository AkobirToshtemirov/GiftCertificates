package com.epam.esm.repository;

import com.epam.esm.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);

    Optional<Role> findByCode(String code);

    List<Role> findAllByUserId(Long userId);
}
