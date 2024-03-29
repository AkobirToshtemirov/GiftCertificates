package com.epam.esm.controller;


import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.TagModel;
import com.epam.esm.model.assembler.TagModelAssembler;
import com.epam.esm.service.TagService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public TagModel getTag(@PathVariable("id") Long id) {
        return tagModelAssembler.toModel(tagService.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id)));
    }

    @GetMapping(value = "/paged")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CollectionModel<TagModel> getTagsWithPage(@RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                     @RequestParam(required = false, defaultValue = "10", name = "size") int size) {

        List<Tag> tags = tagService.findAllWithPage(page, size);

        return tagModelAssembler.toCollectionModel(tags, page, size);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public TagModel saveTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.create(tag);
        return tagModelAssembler.toModel(savedTag);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
