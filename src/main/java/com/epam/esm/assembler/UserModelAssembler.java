package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
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
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
        Link selfLink = linkTo(methodOn(UserController.class).getUser(entity.getId())).withSelfRel();
        Link usersLink = linkTo(methodOn(UserController.class).getUsersWithPage(0, 10)).withRel("users");
        return EntityModel.of(entity, selfLink, usersLink);
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
        List<EntityModel<User>> entityModels = new ArrayList<>();
        entities.forEach(user -> entityModels.add(toModel(user)));
        Link usersLink = linkTo(methodOn(UserController.class).getUsersWithPage(0, 10)).withSelfRel();
        return CollectionModel.of(entityModels, usersLink);
    }

    public CollectionModel<EntityModel<User>> toCollectionModelNoPage(Iterable<? extends User> entities) {
        List<EntityModel<User>> entityModels = new ArrayList<>();
        entities.forEach(user -> entityModels.add(toModel(user)));
        Link usersLink = linkTo(methodOn(UserController.class).getUsers()).withSelfRel();
        return CollectionModel.of(entityModels, usersLink);
    }

    public EntityModel<Tag> toTagModel(Tag entity, Long userId) {
        Link selfLink = linkTo(methodOn(UserController.class).findMostUsedTagOfUserWithHighestOrderCost(userId)).withSelfRel();
        return EntityModel.of(entity, selfLink);
    }
}
