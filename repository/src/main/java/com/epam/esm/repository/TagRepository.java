package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagRepository extends BaseRepository<Tag> {
    Optional<Tag> findByName(String name);
}
