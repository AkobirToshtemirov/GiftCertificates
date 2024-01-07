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
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
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
        CriteriaQuery<Tuple> criteria = cb.createTupleQuery();

        Root<Order> orderRoot = criteria.from(Order.class);
        Join<Order, GiftCertificate> giftCertificateJoin = orderRoot.join("giftCertificate");
        Join<GiftCertificate, Tag> tagJoin = giftCertificateJoin.join("tags");

        criteria.multiselect(
                tagJoin.get("id").alias("tagId"),
                tagJoin.get("name").alias("tagName"),
                cb.sum(orderRoot.get("price")).alias("totalPrice")
        );

        criteria.where(
                cb.equal(orderRoot.get("user").get("id"), userId)
        );
        criteria.groupBy(tagJoin.get("id"), tagJoin.get("name"));
        criteria.orderBy(cb.desc(cb.sum(orderRoot.get("price"))));

        TypedQuery<Tuple> query = entityManager.createQuery(criteria);
        List<Tuple> resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            Tuple result = resultList.get(0);
            Long tagId = result.get("tagId", Long.class);
            String tagName = result.get("tagName", String.class);
            return new Tag(tagId, tagName);
        } else {
            return null;
        }
    }
}
