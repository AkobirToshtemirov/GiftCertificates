package com.epam.esm.controller;

import com.epam.esm.config.security.CustomeUserDetails;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.OrderModel;
import com.epam.esm.model.assembler.OrderModelAssembler;
import com.epam.esm.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/orders", produces = "application/json", consumes = "application/json")
public class OrderController {

    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;

    public OrderController(OrderService orderService, OrderModelAssembler orderModelAssembler) {
        this.orderService = orderService;
        this.orderModelAssembler = orderModelAssembler;
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public OrderModel getOrder(@PathVariable("id") Long id) {
        return orderModelAssembler.toModel(orderService.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id)));
    }

    @GetMapping(value = "/paged")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CollectionModel<OrderModel> getOrdersWithPage(@RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                         @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        List<Order> orders = orderService.findAllWithPage(page, size);
        return orderModelAssembler.toCollectionModel(orders, page, size);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public OrderModel createOrder(@RequestBody OrderDTO dto, Authentication authentication) {
        checkUserPermissionForOrderCreation(dto, authentication);
        Order createdOrder = orderService.create(dto.userId(), dto.giftCertificateId());
        return orderModelAssembler.toModel(createdOrder);
    }

    @GetMapping(value = "/{userId}/orders")
    @PreAuthorize("isAuthenticated()")
    public CollectionModel<OrderModel> getOrdersByUserIdWithPage(@PathVariable("userId") Long userId,
                                                                 @RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                                 @RequestParam(required = false, defaultValue = "10", name = "size") int size,
                                                                 Authentication authentication) {
        checkUserPermissionForOrderView(userId, authentication);
        List<Order> userOrders = orderService.findOrdersInfoByUserIdWithPage(userId, page, size);
        return orderModelAssembler.toCollectionModel(userOrders, page, size);
    }

    @GetMapping(value = "/admin/{userId}/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<OrderModel> getOrdersByUserIdWithPageAdmin(@PathVariable("userId") Long userId,
                                                                      @RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                                      @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        List<Order> userOrders = orderService.findOrdersInfoByUserIdWithPage(userId, page, size);
        return orderModelAssembler.toCollectionModel(userOrders, page, size);
    }

    private void checkUserPermissionForOrderCreation(OrderDTO dto, Authentication authentication) {
        Set<String> roles = getRolesFromAuthentication(authentication);
        if (roles.contains("ROLE_ADMIN")) {
            return;
        }
        checkUserPermissionForOrderView(dto.userId(), authentication);
    }

    private void checkUserPermissionForOrderView(Long userId, Authentication authentication) {
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
        if (!Objects.equals(userId, authenticatedUserId)) {
            throw new AccessDeniedException("You do not have permission to view orders for another user.");
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomeUserDetails userDetails) {
            return userDetails.getUser().getId();
        } else {
            throw new AccessDeniedException("User information not available in the authentication.");
        }
    }

    private Set<String> getRolesFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
