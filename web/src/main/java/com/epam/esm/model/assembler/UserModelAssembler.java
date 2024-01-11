package com.epam.esm.model.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.model.OrderModel;
import com.epam.esm.model.TagModel;
import com.epam.esm.model.UserModel;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends BaseAssembler<User, UserModel> {
    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    @SneakyThrows
    public UserModel toModel(User entity) {
        UserModel userModel = instantiateModel(entity);

        Link popularTag = linkTo(methodOn(UserController.class)
                .findMostUsedTagOfUserWithHighestOrderCost(entity.getId())).withRel("popular_tag");

        userModel.add(createSelfLink(entity.getId()),
                createToAllLink(),
                popularTag);

        userModel.setId(entity.getId());
        userModel.setEmail(entity.getEmail());
        userModel.setUsername(entity.getUsername());
        userModel.setPassword(entity.getPassword());
        userModel.setOrderList(toOrderModel(entity.getOrderList()));

        return userModel;
    }

    @SneakyThrows
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities, int page, int size) {
        CollectionModel<UserModel> userModels = super.toCollectionModel(entities);

        userModels.add(linkTo(methodOn(UserController.class).getUsersWithPage(page, size)).withSelfRel());

        return userModels;
    }

    private List<OrderModel> toOrderModel(List<Order> orders) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .map(this::mapToOrderModel)
                .collect(Collectors.toList());

    }

    private OrderModel mapToOrderModel(Order order) {
        return createOrderModel(order)
                .add(linkTo(OrderController.class, methodOn(OrderController.class)
                        .getOrder(order.getId()))
                        .withSelfRel());
    }

    private OrderModel createOrderModel(Order order) {
        return OrderModel.builder()
                .id(order.getId())
                .orderedTime(order.getOrderedTime())
                .price(order.getPrice())
                .giftCertificateId(order.getGiftCertificate().getId())
                .userId(order.getUser().getId())
                .build();
    }

    @SneakyThrows
    public CollectionModel<UserModel> toCollectionModelNoPage(Iterable<? extends User> entities) {
        List<UserModel> models = new ArrayList<>();
        entities.forEach(user -> models.add(toModel(user)));

        CollectionModel<UserModel> collectionModel = CollectionModel.of(models);
        collectionModel.add(linkTo(UserController.class).withSelfRel());
        return collectionModel;
    }

    @SneakyThrows
    public TagModel toTagModel(Tag tag, Long userId) {
        TagModel tagModel = TagModel.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();

        tagModel.add(linkTo(methodOn(TagController.class)
                .getTag(tag.getId()))
                .withSelfRel());

        tagModel.add(linkTo(methodOn(UserController.class)
                .getUser(userId))
                .withRel("user"));

        return tagModel;
    }
}