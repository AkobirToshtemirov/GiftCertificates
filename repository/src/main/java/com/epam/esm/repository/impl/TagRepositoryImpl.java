package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.validator.EntityValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link TagRepository} interface.
 */
@Repository
public class TagRepositoryImpl implements TagRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final EntityValidator entityValidator;

    public TagRepositoryImpl(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag save(Tag entity) {
        entityValidator.validateEntity(entity);

        entityManager.persist(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws NotFoundException {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null)
            throw new NotFoundException("Tag not found with id: " + id);
        entityManager.remove(tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Tag> findByName(String name) {
        return entityManager.createQuery("SELECT t FROM tags t WHERE t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tag> findMostUsedTagOfUserWithHighestOrderCost(Long userId) {
        String nativeQuery = "SELECT t.id, t.name " +
                "FROM orders o " +
                "JOIN gift_certificates g ON g.id = o.gift_certificate_id " +
                "JOIN gift_certificate_tag gct ON g.id = gct.gift_id " +
                "JOIN tags t ON t.id = gct.tag_id " +
                "WHERE o.user_id = :userId " +
                "GROUP BY t.id, t.name " +
                "HAVING SUM(o.price) = (SELECT MAX(total_price) " +
                "                     FROM (SELECT SUM(o.price) AS total_price " +
                "                           FROM orders o " +
                "                           JOIN gift_certificates g ON g.id = o.gift_certificate_id " +
                "                           JOIN gift_certificate_tag gct ON g.id = gct.gift_id " +
                "                           JOIN tags t ON t.id = gct.tag_id " +
                "                           WHERE o.user_id = :userId " +
                "                           GROUP BY t.id, t.name) subquery)";

        Query query = entityManager.createNativeQuery(nativeQuery)
                .setParameter("userId", userId);

        List<Object[]> resultList = query.getResultList();

        List<Tag> tags = new ArrayList<>();

        for (Object[] result : resultList) {
            Long tagId = (Long) result[0];
            String tagName = (String) result[1];

            tags.add(new Tag(tagId, tagName));
        }

        return tags;
    }
}
