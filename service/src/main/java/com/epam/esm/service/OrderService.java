package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {

    /**
     * Create a new order.
     *
     * @param dto            the order DTO containing user and gift certificate information
     * @param authentication the authentication object
     * @return the created order
     */
    Order createOrder(OrderDTO dto, Authentication authentication);

    /**
     * Get all orders with pagination.
     *
     * @param page the page number
     * @param size the page size
     * @return a list of orders with pagination
     */
    List<Order> findAllWithPage(int page, int size);

    /**
     * Get an order by its ID.
     *
     * @param id the order ID
     * @return the order with the specified ID
     */
    Order findById(Long id, Authentication authentication);

    /**
     * Get orders by user ID with pagination.
     *
     * @param page           the page number
     * @param size           the page size
     * @param authentication the authentication object
     * @return a list of orders with pagination for the specified user
     */
    List<Order> findOrdersByUserIdWithPage(int page, int size, Authentication authentication);

    /**
     * Get orders by user ID with pagination (for admin use).
     *
     * @param userId the user ID
     * @param page   the page number
     * @param size   the page size
     * @return a list of orders with pagination for the specified user
     */
    List<Order> findOrdersByUserIdWithPageAdmin(Long userId, int page, int size);

    /**
     * Get all orders for a user.
     *
     * @param userId the user ID
     * @return a list of all orders for the specified user
     */
    List<Order> findOrdersByUserId(Long userId);
}
