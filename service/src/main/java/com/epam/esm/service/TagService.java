package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagService {
    Tag createTag(Tag tag);

    List<Tag> findAllTags();

    Tag findTagById(Long id);

    Tag findTagByName(String name);

    void deleteTag(Long id);
}
