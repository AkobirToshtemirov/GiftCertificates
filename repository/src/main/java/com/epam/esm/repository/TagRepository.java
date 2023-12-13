package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.exception.TagOperationException;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag) throws TagOperationException;

    Optional<Tag> findById(Long id) throws TagNotFoundException;

    Optional<Tag> findByName(String name);

    List<Tag> findAll() throws TagNotFoundException;

    void delete(Long id) throws TagOperationException;
}
