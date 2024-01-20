package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.model.OrderModel;
import com.epam.esm.model.assembler.OrderModelAssembler;
import com.epam.esm.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        return orderModelAssembler.toModel(orderService.findById(id));
    }

    @GetMapping(value = "/paged")
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<OrderModel> getOrdersWithPage(
            @RequestParam(required = false, defaultValue = "1", name = "page") int page,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        List<Order> orders = orderService.findAllWithPage(page, size);
        return orderModelAssembler.toCollectionModel(orders, page, size);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public OrderModel createOrder(@RequestBody OrderDTO dto, Authentication authentication) {
        return orderModelAssembler.toModel(orderService.createOrder(dto, authentication));
    }

    @GetMapping(value = "/{userId}/user-orders")
    @PreAuthorize("isAuthenticated()")
    public CollectionModel<OrderModel> getOrdersByUserIdWithPage(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false, defaultValue = "1", name = "page") int page,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size,
            Authentication authentication) {
        return orderModelAssembler.toCollectionModel(orderService.findOrdersByUserIdWithPage(userId, page, size, authentication), page, size);
    }

    @GetMapping(value = "/admin/{userId}/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<OrderModel> getOrdersByUserIdWithPageAdmin(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false, defaultValue = "1", name = "page") int page,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        return orderModelAssembler.toCollectionModel(orderService.findOrdersByUserIdWithPageAdmin(userId, page, size), page, size);
    }
}

