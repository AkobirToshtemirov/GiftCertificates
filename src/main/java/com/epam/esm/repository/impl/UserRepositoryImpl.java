package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.validator.EntityValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final EntityValidator entityValidator;

    public UserRepositoryImpl(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    @Override
    public User save(User entity) {
        entityValidator.validateEntity(entity);

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM users u", User.class).getResultList();
    }

    @Override
    public List<User> findAllWithPage(int page, int size) throws ValidationException {
        if (page <= 0 || size <= 0)
            throw new ValidationException("Page number and page size must be positive");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);

        query.select(userRoot);

        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        User user = entityManager.find(User.class, id);
        if (user == null)
            throw new NotFoundException("User not found with id: " + id);
        entityManager.remove(user);
    }

    @Override
    public Tag findMostUsedTagOfUserWithHighestOrderCost(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = cb.createQuery(Tag.class);
        Root<User> userRoot = query.from(User.class);

        Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
        Root<Order> orderRoot = subquery.from(Order.class);
        Join<Order, GiftCertificate> certificateJoin = orderRoot.join("giftCertificate");

        Predicate userPredicate = cb.equal(userRoot.get("id"), userId);
        Predicate userOrderPredicate = cb.equal(orderRoot.get("user"), userRoot);

        Expression<BigDecimal> sumOfOrderCost = cb.sum(certificateJoin.get("price"));
        subquery.select(sumOfOrderCost)
                .where(userOrderPredicate)
                .groupBy(orderRoot.get("user"));

        query.select(userRoot.get("orderList").get("giftCertificate").get("tags"))
                .where(cb.and(userPredicate, cb.equal(orderRoot.get("price"), subquery)))
                .orderBy(cb.desc(sumOfOrderCost));

        List<Tag> tags = entityManager.createQuery(query)
                .setMaxResults(1)
                .getResultList();

        return (tags != null && !tags.isEmpty()) ? tags.get(0) : null;
    }
}