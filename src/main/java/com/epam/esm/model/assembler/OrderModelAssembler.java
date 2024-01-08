package com.epam.esm.model.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.entity.Order;
import com.epam.esm.model.OrderModel;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler extends BaseAssembler<Order, OrderModel> {
    public OrderModelAssembler() {
        super(OrderController.class, OrderModel.class);
    }

    @Override
    @SneakyThrows
    public OrderModel toModel(Order entity) {
        OrderModel orderModel = instantiateModel(entity);

        orderModel.add(createSelfLink(entity.getId()),
                createPostLink(),
                createUserLink(entity.getUser().getId()),
                createGiftCertificateLink(entity.getGiftCertificate().getId()));

        setModelAttributes(orderModel, entity);
        return orderModel;
    }

    @SneakyThrows
    public CollectionModel<OrderModel> toCollectionModel(Iterable<? extends Order> entities, int page, int size) {
        CollectionModel<OrderModel> orderModels = super.toCollectionModel(entities);

        orderModels.add(linkTo(methodOn(TagController.class).getTagsWithPage(page, size)).withSelfRel());

        return orderModels;
    }

    private Link createUserLink(Long userId) {
        return linkTo(methodOn(UserController.class).getUser(userId))
                .withRel("user")
                .withType(String.valueOf(HttpMethod.GET));
    }

    private Link createGiftCertificateLink(Long giftCertificateId) {
        return linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(giftCertificateId))
                .withRel("giftCertificate")
                .withType(String.valueOf(HttpMethod.GET));
    }

    private void setModelAttributes(OrderModel orderModel, Order entity) {
        orderModel.setId(entity.getId());
        orderModel.setPrice(entity.getPrice());
        orderModel.setOrderedTime(entity.getOrderedTime());
        orderModel.setUserId(entity.getUser().getId());
        orderModel.setGiftCertificateId(entity.getGiftCertificate().getId());
    }

    @SneakyThrows
    public CollectionModel<OrderModel> toCollectionModelNoPage(Iterable<? extends Order> entities) {
        List<OrderModel> models = new ArrayList<>();
        entities.forEach(order -> models.add(toModel(order)));

        CollectionModel<OrderModel> collectionModel = CollectionModel.of(models);
        collectionModel.add(linkTo(OrderController.class).withSelfRel());
        return collectionModel;
    }
}