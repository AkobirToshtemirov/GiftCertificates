package com.epam.esm.service;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for CRUD operations on Order entities.
 */
public interface OrderService {
    /**
     * Creates an Order for the specified user and gift certificate.
     *
     * @param userId            the ID of the user
     * @param giftCertificateId the ID of the gift certificate
     * @return the created Order
     */
    Order create(Long userId, Long giftCertificateId);

    /**
     * Retrieves a paginated list of Orders.
     *
     * @param page the page number (0-based)
     * @param size the number of entities per page
     * @return a list of Orders for the specified page
     */
    List<Order> findAllWithPage(int page, int size);

    /**
     * Retrieves an Order by its ID.
     *
     * @param id the ID of the Order to be retrieved
     * @return an {@code Optional} containing the Order, or empty if not found
     */
    Optional<Order> findById(Long id);

    /**
     * Retrieves a paginated list of Orders with additional information by user ID.
     *
     * @param userId the ID of the user
     * @param page   the page number (0-based)
     * @param size   the number of entities per page
     * @return a paginated list of Orders with additional information for the specified user ID
     */
    List<Order> findOrdersInfoByUserIdWithPage(Long userId, int page, int size);

    /**
     * Retrieves a list of Orders with additional information by user ID.
     *
     * @param userId the ID of the user
     * @return a list of Orders with additional information for the specified user ID
     */
    List<Order> findOrdersInfoByUserId(Long userId);
}
