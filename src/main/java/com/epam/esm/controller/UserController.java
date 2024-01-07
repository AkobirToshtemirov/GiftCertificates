package com.epam.esm.controller;

import com.epam.esm.assembler.UserModelAssembler;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public EntityModel<User> getUser(@PathVariable("id") Long id) {
        return userModelAssembler.toModel(userService.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id)));
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> getUsers() {
        List<User> users = userService.findAll();
        return userModelAssembler.toCollectionModelNoPage(users);
    }

    @GetMapping("/paged")
    public CollectionModel<EntityModel<User>> getUsersWithPage(@RequestParam(required = false, defaultValue = "1") int page,
                                                               @RequestParam(required = false, defaultValue = "10") int size) {
        List<User> users = userService.findAllWithPage(page, size);

        return userModelAssembler.toCollectionModel(users, page, size);
    }

    @PostMapping()
    public EntityModel<User> saveUser(@RequestBody User user) {
        User savedUser = userService.create(user);
        return userModelAssembler.toModel(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/most-used-tag")
    public EntityModel<Tag> findMostUsedTagOfUserWithHighestOrderCost(@PathVariable("id") Long userId) {
        Tag mostUsedTag = userService.findMostUsedTagOfUserWithHighestOrderCost(userId);
        return userModelAssembler.toTagModel(mostUsedTag, userId);
    }
}
