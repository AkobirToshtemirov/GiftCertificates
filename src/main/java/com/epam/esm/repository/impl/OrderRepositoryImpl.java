package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.validator.EntityValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final EntityValidator entityValidator;

    public OrderRepositoryImpl(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    @Override
    public Order save(Order entity) {
        entityValidator.validateEntity(entity);

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public List<Order> findAll() {
        return entityManager.createQuery("SELECT o FROM orders o", Order.class).getResultList();
    }

    @Override
    public List<Order> findAllWithPage(int page, int size) throws ValidationException{
        if (page <= 0 || size <= 0)
            throw new ValidationException("Page number and page size must be positive");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> orderRoot = query.from(Order.class);

        query.select(orderRoot);

        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Order order = entityManager.find(Order.class, id);
        if (order == null)
            throw new NotFoundException("Order not found with id: " + id);
        entityManager.remove(order);
    }

    @Override
    public List<Order> findOrdersInfoByUserIdWithPage(Long userId, int page, int size) {
        if (page < 0 || size <= 0)
            throw new ValidationException("Page number and page size must be positive");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> orderRoot = query.from(Order.class);

        query.select(orderRoot)
                .where(cb.equal(orderRoot.get("user").get("id"), userId));

        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    @Override
    public List<Order> findOrdersInfoByUserId(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> orderRoot = query.from(Order.class);

        query.select(orderRoot)
                .where(cb.equal(orderRoot.get("user").get("id"), userId));

        return entityManager.createQuery(query).getResultList();
    }
}
