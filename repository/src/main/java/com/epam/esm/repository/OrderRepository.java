package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;

/**
 * Repository interface for CRUD operations on Order entities.
 */
public interface OrderRepository extends BaseRepository<Order> {
    /**
     * Retrieves a paginated list of Order entities with additional information by user ID.
     *
     * @param userId the ID of the user
     * @param page   the page number (0-based)
     * @param size   the number of entities per page
     * @return a paginated list of Order entities with additional information for the specified user ID
     */
    List<Order> findOrdersInfoByUserIdWithPage(Long userId, int page, int size);

    /**
     * Retrieves a list of Order entities with additional information by user ID.
     *
     * @param userId the ID of the user
     * @return a list of Order entities with additional information for the specified user ID
     */
    List<Order> findOrdersInfoByUserId(Long userId);
}
