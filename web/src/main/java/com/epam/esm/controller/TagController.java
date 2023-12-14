package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.BaseService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TagController handles HTTP requests related to tags, providing endpoints for tag creation, retrieval, and deletion.
 * This controller communicates with the {@link com.epam.esm.service.TagService} for tag-related business logic.
 */
@RestController
@RequestMapping(
        path = "/tags",
        consumes = {"application/json"},
        produces = {"application/json"}
)
public class TagController {
    private final BaseService<Tag> tagBaseService;

    @Autowired
    public TagController(BaseService<Tag> tagBaseService) {
        this.tagBaseService = tagBaseService;
    }

    /**
     * Creates a new tag.
     *
     * @param tag The tag to be created.
     * @return ResponseEntity containing the created tag and HTTP status code 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagBaseService.create(tag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * Retrieves a tag by its ID.
     *
     * @param id The ID of the tag to be retrieved.
     * @return ResponseEntity containing the retrieved tag and HTTP status code 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> findTagById(@PathVariable Long id) {
        Tag tag = tagBaseService.findById(id);
        return ResponseEntity.ok(tag);
    }

    /**
     * Retrieves all tags.
     *
     * @return ResponseEntity containing the list of all tags and HTTP status code 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Tag>> findAllTags() {
        List<Tag> tags = tagBaseService.findAll();
        return ResponseEntity.ok(tags);
    }

    /**
     * Deletes a tag by its ID.
     *
     * @param id The ID of the tag to be deleted.
     * @return ResponseEntity with HTTP status code 204 (No Content) indicating successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagBaseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
