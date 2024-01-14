package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.OrderModel;
import com.epam.esm.model.assembler.OrderModelAssembler;
import com.epam.esm.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public OrderModel getOrder(@PathVariable("id") Long id) {
        return orderModelAssembler.toModel(orderService.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id)));
    }

    @GetMapping(value = "/paged")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public CollectionModel<OrderModel> getOrdersWithPage(@RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                         @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        List<Order> orders = orderService.findAllWithPage(page, size);

        return orderModelAssembler.toCollectionModel(orders, page, size);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public OrderModel createOrder(@RequestBody OrderDTO dto) {
        Order createdOrder = orderService.create(dto.userId(), dto.giftCertificateId());
        return orderModelAssembler.toModel(createdOrder);
    }

    @GetMapping(value = "/{userId}/user-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public CollectionModel<OrderModel> getOrdersByUserIdWithPage(@PathVariable("userId") Long userId,
                                                                 @RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                                 @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        List<Order> userOrders = orderService.findOrdersInfoByUserIdWithPage(userId, page, size);
        return orderModelAssembler.toCollectionModel(userOrders, page, size);
    }
}
