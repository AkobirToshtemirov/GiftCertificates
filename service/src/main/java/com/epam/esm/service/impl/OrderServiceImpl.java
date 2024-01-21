package com.epam.esm.service.impl;

import com.epam.esm.config.security.CustomeUserDetails;
import com.epam.esm.dto.OrderDTO;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    @Transactional
    public Order createOrder(OrderDTO dto, Authentication authentication) {
        validateOrderCreation(dto);
        Long userId = getUserIdFromAuthentication(authentication);
        User user = validateUserAndGet(userId);
        GiftCertificate giftCertificate = validateGiftCertificateAndGet(dto.giftCertificateId());

        checkUserPermissionForOrderCreation(user.getId(), authentication);

        Order order = new Order();
        order.setGiftCertificate(giftCertificate);
        order.setUser(user);
        order.setOrderedTime(LocalDateTime.now());
        order.setPrice(giftCertificate.getPrice());

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAllWithPage(int page, int size) {
        validatePageAndSize(page, size);
        return orderRepository.findAllWithPage(page, size);
    }

    @Override
    public Order findById(Long id, Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        Long userIdFromAuthentication = getUserIdFromAuthentication(authentication);

        if (!Objects.equals(order.getUser().getId(), userIdFromAuthentication)) {
            throw new AccessDeniedException("You do not have permission to view orders for another user.");
        }

        if (!isUserAdmin(authentication)) {
            if (!Objects.equals(order.getUser().getId(), userIdFromAuthentication)) {
                throw new AccessDeniedException("You do not have permission to view orders for another user.");
            }
        }

        return order;
    }

    @Override
    public List<Order> findOrdersByUserIdWithPage(int page, int size, Authentication authentication) {
        validatePageAndSize(page, size);
        Long userId = getUserIdFromAuthentication(authentication);
        User user = validateUserAndGet(userId);

        checkUserPermissionForOrderView(user.getId(), userId);

        return orderRepository.findOrdersInfoByUserIdWithPage(userId, page, size);
    }

    @Override
    public List<Order> findOrdersByUserIdWithPageAdmin(Long userId, int page, int size) {
        validatePageAndSize(page, size);
        validateUserAndGet(userId);
        return orderRepository.findOrdersInfoByUserIdWithPage(userId, page, size);
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        validateUserAndGet(userId);
        return orderRepository.findOrdersInfoByUserId(userId);
    }

    private void validateOrderCreation(OrderDTO dto) {
        if (Objects.isNull(dto.giftCertificateId())) {
            throw new ValidationException("GiftCertificate Id cannot be empty!");
        }
    }

    private User validateUserAndGet(Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found with id: " + userId));
    }

    private GiftCertificate validateGiftCertificateAndGet(Long giftCertificateId) {
        return giftCertificateService.findById(giftCertificateId)
                .orElseThrow(() -> new NotFoundException("Gift Certificate is not found with id: " + giftCertificateId));
    }

    private void checkUserPermissionForOrderView(Long userId, Long authenticatedUserId) {
        if (!Objects.equals(userId, authenticatedUserId)) {
            throw new AccessDeniedException("You do not have permission to view orders for another user.");
        }
    }

    private void checkUserPermissionForOrderCreation(Long userId, Authentication authentication) {
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
        if (!Objects.equals(userId, authenticatedUserId)) {
            throw new AccessDeniedException("You do not have permission to create an order for another user.");
        }
    }

    private void validatePageAndSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new ValidationException("Page number and page size must be positive");
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        return Optional.ofNullable(authentication.getPrincipal())
                .filter(principal -> principal instanceof CustomeUserDetails)
                .map(principal -> ((CustomeUserDetails) principal).getUser().getId())
                .orElseThrow(() -> new AccessDeniedException("User information not available in the authentication."));
    }

    private boolean isUserAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }
}
