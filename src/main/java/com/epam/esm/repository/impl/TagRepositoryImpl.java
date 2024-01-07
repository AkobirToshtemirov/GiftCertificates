package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.validator.EntityValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final EntityValidator entityValidator;

    public TagRepositoryImpl(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    @Override
    public Tag save(Tag entity) {
        entityValidator.validateEntity(entity);

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public List<Tag> findAll() {
        return entityManager.createQuery("SELECT t FROM tags t", Tag.class).getResultList();
    }

    @Override
    public List<Tag> findAllWithPage(int page, int size) throws ValidationException {
        if (page <= 0 || size <= 0)
            throw new ValidationException("Page number and page size must be positive");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = cb.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);

        query.select(tagRoot);

        TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null)
            throw new NotFoundException("Tag not found with id: " + id);
        entityManager.remove(tag);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return entityManager.createQuery("SELECT t FROM tags t WHERE t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }
}
