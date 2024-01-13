package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag create(Tag entity) {
        return tagRepository.save(entity);
    }

    @Override
    public List<Tag> findAllWithPage(int page, int size) {
        return tagRepository.findAllWithPage(page, size);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        try {
            tagRepository.delete(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Tag not found with id: " + id);
        }
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }
}
