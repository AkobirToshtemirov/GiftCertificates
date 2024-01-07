package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.entity.Order;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order entity) {
        Link selfLink = linkTo(methodOn(OrderController.class).getOrder(entity.getId())).withSelfRel();
        Link ordersLink = linkTo(methodOn(OrderController.class).getOrdersWithPage(1, 10)).withRel("orders");
        return EntityModel.of(entity, selfLink, ordersLink);
    }

    @Override
    public CollectionModel<EntityModel<Order>> toCollectionModel(Iterable<? extends Order> entities) {
        List<EntityModel<Order>> entityModels = new ArrayList<>();
        entities.forEach(order -> entityModels.add(toModel(order)));
        Link ordersLink = linkTo(methodOn(OrderController.class).getOrdersWithPage(1, 10)).withSelfRel();
        return CollectionModel.of(entityModels, ordersLink);
    }

    public CollectionModel<EntityModel<Order>> toCollectionModelNoPage(Iterable<? extends Order> entities) {
        List<EntityModel<Order>> entityModels = new ArrayList<>();
        entities.forEach(order -> entityModels.add(toModel(order)));
        Link ordersLink = linkTo(methodOn(OrderController.class).getOrders()).withSelfRel();
        return CollectionModel.of(entityModels, ordersLink);
    }
}
