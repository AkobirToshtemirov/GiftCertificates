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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final EntityValidator entityValidator;

    public GiftCertificateRepositoryImpl(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    @Override
    public GiftCertificate save(GiftCertificate entity) {
        entityValidator.validateEntity(entity);

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public List<GiftCertificate> findAll() {
        return entityManager.createQuery("SELECT g FROM gift_certificates g", GiftCertificate.class).getResultList();
    }

    @Override
    public List<GiftCertificate> findAllWithPage(int page, int size) throws ValidationException {
        if (page <= 0 || size <= 0)
            throw new ValidationException("Page number and page size must be positive");

        return entityManager.createQuery("SELECT g FROM gift_certificates g", GiftCertificate.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (giftCertificate == null)
            throw new NotFoundException("GiftCertificate not found with id: " + id);
        entityManager.remove(giftCertificate);
    }

    @Override
    public void update(GiftCertificate giftCertificate) throws NotFoundException, OperationException {
        entityValidator.validateEntity(giftCertificate);

        GiftCertificate existingCertificate = entityManager.find(GiftCertificate.class, giftCertificate.getId());
        if (existingCertificate == null)
            throw new NotFoundException("Gift certificate not found with ID: " + giftCertificate.getId());

        try {
            entityManager.merge(giftCertificate);
        } catch (Exception ex) {
            throw new OperationException("Failed to update gift certificate: " + ex.getMessage(), ex);
        }

    }

    @Override
    public List<GiftCertificate> findCertificatesByCriteria(String[] tagNames, String search, String sortBy, boolean ascending) throws OperationException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);

        List<Predicate> predicates = new ArrayList<>();

        // Adding criteria based on tag names
        if (tagNames != null && tagNames.length > 0) {
            Join<GiftCertificate, Tag> tagJoin = certificateRoot.join("tags");
            List<Predicate> tagPredicates = Arrays.stream(tagNames)
                    .map(tagName -> criteriaBuilder.equal(tagJoin.get("name"), tagName))
                    .collect(Collectors.toList());
            predicates.add(criteriaBuilder.and(tagPredicates.toArray(new Predicate[0])));
        }

        // Adding criteria based on search string
        if (search != null && !search.isEmpty()) {
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(certificateRoot.get("name"), "%" + search + "%"),
                    criteriaBuilder.like(certificateRoot.get("description"), "%" + search + "%")
            );
            predicates.add(searchPredicate);
        }

        // Creating sorting order
        if (sortBy != null && !sortBy.isEmpty()) {
            if (ascending) {
                query.orderBy(criteriaBuilder.asc(certificateRoot.get(sortBy)));
            } else {
                query.orderBy(criteriaBuilder.desc(certificateRoot.get(sortBy)));
            }
        }

        // Adding all predicates to the where clause
        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
