package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
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
    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent())
            return tag.get();
        throw new TagNotFoundException("Tag not found with the id: " + id);
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(id);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }
}
