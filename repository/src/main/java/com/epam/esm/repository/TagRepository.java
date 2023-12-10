package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag) throws OperationException;

    Optional<Tag> findById(Long id) throws NotFoundException;

    Optional<Tag> findByName(String name) throws NotFoundException;

    List<Tag> findAll() throws NotFoundException;

    void delete(Long id) throws OperationException;
}
