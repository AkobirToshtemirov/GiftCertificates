package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findTagById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent())
            return tag.get();
        throw new NotFoundException("Tag not found with the id: " + id);
    }

    @Override
    public Tag findTagByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        if (tag.isPresent())
            return tag.get();
        throw new NotFoundException("Tag not found with the name: " + name);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.delete(id);
    }
}
