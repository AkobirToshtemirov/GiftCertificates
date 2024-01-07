package com.epam.esm.controller;


import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tags", produces = "application/json", consumes = "application/json")
public class TagController {
    private final TagService tagService;
    private final TagModelAssembler tagModelAssembler;

    public TagController(TagService tagService, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
    }

    @GetMapping(value = "/{id}")
    public EntityModel<Tag> getTag(@PathVariable("id") Long id) {
        return tagModelAssembler.toModel(tagService.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id)));
    }

    @GetMapping
    public CollectionModel<EntityModel<Tag>> getTags() {
        List<Tag> tags = tagService.findAll();
        return tagModelAssembler.toCollectionModelNoPage(tags);
    }

    @GetMapping("/paged")
    public CollectionModel<EntityModel<Tag>> getTagsWithPage(@RequestParam(required = false, defaultValue = "0") int page,
                                                             @RequestParam(required = false, defaultValue = "10") int size) {

        List<Tag> tags = tagService.findAllWithPage(page, size);
        CollectionModel<EntityModel<Tag>> collectionModel = tagModelAssembler.toCollectionModel(tags);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(tags.size()));

        return collectionModel;
    }

    @PostMapping()
    public EntityModel<Tag> saveTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.create(tag);
        return tagModelAssembler.toModel(savedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // to user

//    @GetMapping("user/{id}")
//    public EntityModel<Tag> getPopularTag(@PathVariable("id") Long userId) {
//        Tag popularTag = tagService.find(userId);
//        return tagModelAssembler.toModel(popularTag);
//    }
}
