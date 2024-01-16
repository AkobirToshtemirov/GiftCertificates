package com.epam.esm.service.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.repository.impl.RoleRepositoryImpl;
import com.epam.esm.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link RoleService} interface.
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepositoryImpl roleRepository;

    public RoleServiceImpl(RoleRepositoryImpl roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
