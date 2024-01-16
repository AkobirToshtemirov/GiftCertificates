package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.validator.EntityValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link GiftCertificateRepository} interface.
 */

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final EntityValidator entityValidator;

    public GiftCertificateRepositoryImpl(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GiftCertificate save(GiftCertificate entity) {
        entityValidator.validateEntity(entity);

        entityManager.persist(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GiftCertificate> findAllWithPage(int page, int size) throws ValidationException {
        if (page <= 0 || size <= 0)
            throw new ValidationException("Page number and page size must be positive");

        return entityManager.createQuery("SELECT g FROM gift_certificates g", GiftCertificate.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws NotFoundException {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (giftCertificate == null)
            throw new NotFoundException("GiftCertificate not found with id: " + id);
        entityManager.remove(giftCertificate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) throws NotFoundException, OperationException {
        entityValidator.validateEntity(giftCertificate);

        GiftCertificate existingCertificate = entityManager.find(GiftCertificate.class, giftCertificate.getId());
        if (existingCertificate == null)
            throw new NotFoundException("Gift certificate not found with ID: " + giftCertificate.getId());

        try {
            return entityManager.merge(giftCertificate);
        } catch (Exception ex) {
            throw new OperationException("Failed to update gift certificate: " + ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GiftCertificate> findCertificatesByCriteria(List<String> tagNames, String search, String sortBy, boolean ascending) throws OperationException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);

        Predicate finalPredicate = criteriaBuilder.conjunction();
        List<Predicate> tagPredicates = new ArrayList<>();

        if (tagNames != null && !tagNames.isEmpty()) {
            for (String tagName : tagNames) {
                Join<GiftCertificate, Tag> tagJoin = certificateRoot.join("tags");
                Predicate tagPredicate = criteriaBuilder.equal(tagJoin.get("name"), tagName);
                tagPredicates.add(tagPredicate);
            }
            finalPredicate = criteriaBuilder.and(tagPredicates.toArray(new Predicate[0]));
        }

        if (search != null && !search.isEmpty()) {
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(certificateRoot.get("name"), "%" + search + "%"),
                    criteriaBuilder.like(certificateRoot.get("description"), "%" + search + "%")
            );
            finalPredicate = criteriaBuilder.and(finalPredicate, searchPredicate);
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            Expression<?> orderField = sortBy.equals("name") ? certificateRoot.get("name") : certificateRoot.get("createdDate");
            query.orderBy(ascending ? criteriaBuilder.asc(orderField) : criteriaBuilder.desc(orderField));
        }

        query.where(finalPredicate);

        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
