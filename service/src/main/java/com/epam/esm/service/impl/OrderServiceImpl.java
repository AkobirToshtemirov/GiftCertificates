package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the {@link OrderService} interface.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, GiftCertificateService giftCertificateService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Order create(Long userId, Long giftCertificateId) {
        if (Objects.isNull(userId)) {
            throw new ValidationException("User Id cannot be empty!");
        }
        if (Objects.isNull(giftCertificateId)) {
            throw new ValidationException("GiftCertificate Id cannot be empty!");
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found with id: " + userId));

        GiftCertificate giftCertificate = giftCertificateService.findById(giftCertificateId)
                .orElseThrow(() -> new NotFoundException("Gift Certificate is not found with id: " + giftCertificateId));

        Order order = new Order();
        order.setGiftCertificate(giftCertificate);
        order.setUser(user);
        order.setOrderedTime(LocalDateTime.now());
        order.setPrice(giftCertificate.getPrice());

        return orderRepository.save(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> findAllWithPage(int page, int size) {
        try {
            return orderRepository.findAllWithPage(page, size);
        } catch (ValidationException e) {
            throw new ValidationException("Page number and page size must be positive");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> findOrdersInfoByUserIdWithPage(Long userId, int page, int size) {
        userService.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        try {
            return orderRepository.findOrdersInfoByUserIdWithPage(userId, page, size);
        } catch (ValidationException e) {
            throw new ValidationException("Page number and page size must be positive");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> findOrdersInfoByUserId(Long userId) {
        userService.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return orderRepository.findOrdersInfoByUserId(userId);
    }
}