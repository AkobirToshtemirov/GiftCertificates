package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.repository.BaseRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.BaseService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements BaseService<Tag>, TagService {
    private final TagRepository tagRepository;
    private final BaseRepository<Tag> baseRepository;


    @Autowired
    public TagServiceImpl(TagRepository tagRepository, BaseRepository<Tag> baseRepository) {
        this.tagRepository = tagRepository;
        this.baseRepository = baseRepository;
    }

    @Override
    public Tag create(Tag tag) {
        Optional<Tag> existingTag = tagRepository.findByName(tag.getName());
        return existingTag.orElse(baseRepository.save(tag));
    }

    @Override
    public List<Tag> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public Tag findById(Long id) {
        Optional<Tag> tag = baseRepository.findById(id);
        if (tag.isPresent())
            return tag.get();
        throw new TagNotFoundException("Tag not found with the id: " + id);
    }

    @Override
    public void delete(Long id) {
        baseRepository.delete(id);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }
}
