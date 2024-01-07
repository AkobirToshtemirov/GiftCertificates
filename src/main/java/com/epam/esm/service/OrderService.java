package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order create(Long userId, Long giftCertificateId) throws NotFoundException;

    List<Order> findAll();

    List<Order> findAllWithPage(int page, int size);

    Optional<Order> findById(Long id);

    List<Order> findOrdersInfoByUserIdWithPage(Long userId, int page, int size);

    List<Order> findOrdersInfoByUserId(Long userId);
}
