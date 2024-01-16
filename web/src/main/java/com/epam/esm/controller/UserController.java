package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.TagModel;
import com.epam.esm.model.UserModel;
import com.epam.esm.model.assembler.UserModelAssembler;
import com.epam.esm.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json", consumes = "application/json")
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserModel getUser(@PathVariable("id") Long id) {
        return userModelAssembler.toModel(userService.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id)));
    }

    @GetMapping(value = "/paged")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CollectionModel<UserModel> getUsersWithPage(@RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                       @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        List<User> users = userService.findAllWithPage(page, size);

        return userModelAssembler.toCollectionModel(users, page, size);
    }

    @GetMapping("/{id}/most-used-tag")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CollectionModel<TagModel> findMostUsedTagOfUserWithHighestOrderCost(@PathVariable("id") Long userId) {
        List<Tag> mostUsedTag = userService.findMostUsedTagOfUserWithHighestOrderCost(userId);
        return userModelAssembler.toCollectionTagModel(mostUsedTag, userId);
    }
}
