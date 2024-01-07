package com.epam.esm.controller;

import com.epam.esm.assembler.OrderModelAssembler;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
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
    public EntityModel<Order> getOrder(@PathVariable("id") Long id) {
        return orderModelAssembler.toModel(orderService.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id)));
    }

    @GetMapping
    public CollectionModel<EntityModel<Order>> getOrders() {
        List<Order> orders = orderService.findAll();
        return orderModelAssembler.toCollectionModelNoPage(orders);
    }

    @GetMapping("/paged")
    public CollectionModel<EntityModel<Order>> getOrdersWithPage(@RequestParam(required = false, defaultValue = "0") int page,
                                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        List<Order> orders = orderService.findAllWithPage(page, size);
        CollectionModel<EntityModel<Order>> collectionModel = orderModelAssembler.toCollectionModel(orders);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(orders.size()));

        return collectionModel;
    }

    @PostMapping
    public EntityModel<Order> createOrder(@RequestBody OrderDTO dto) {
        Order createdOrder = orderService.create(dto.userId(), dto.giftCertificateId());
        return orderModelAssembler.toModel(createdOrder);
    }

    @GetMapping("/{userId}/user-orders")
    public CollectionModel<EntityModel<Order>> getOrdersByUserIdWithPage(@PathVariable Long userId,
                                                                         @RequestParam(required = false, defaultValue = "0") int page,
                                                                         @RequestParam(required = false, defaultValue = "10") int size) {
        List<Order> userOrders = orderService.findOrdersInfoByUserIdWithPage(userId, page, size);
        return orderModelAssembler.toCollectionModel(userOrders);
    }

    @GetMapping("/{userId}/user-orders/all")
    public CollectionModel<EntityModel<Order>> getAllOrdersByUserId(@PathVariable Long userId) {
        List<Order> userOrders = orderService.findOrdersInfoByUserId(userId);
        return orderModelAssembler.toCollectionModelNoPage(userOrders);
    }
}
