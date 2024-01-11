package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderRepository extends BaseRepository<Order> {
    List<Order> findOrdersInfoByUserIdWithPage(Long userId, int page, int size);

    List<Order> findOrdersInfoByUserId(Long userId);
}
