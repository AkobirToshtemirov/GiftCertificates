package com.epam.esm.repository.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.validator.EntityValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link RoleRepository} interface.
 */
@Repository
public class RoleRepositoryImpl implements RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final EntityValidator entityValidator;

    public RoleRepositoryImpl(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role save(Role role) {
        entityValidator.validateEntity(role);

        entityManager.persist(role);
        return role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Role> findByCode(String code) {
        String jpql = "SELECT r FROM Role r WHERE r.code = :code";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        query.setParameter("code", code);

        try {
            Role role = query.getSingleResult();
            return Optional.of(role);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Role> findAllByUserId(Long userId) {
        String jpql = "SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
