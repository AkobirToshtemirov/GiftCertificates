package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateTagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    private final GiftCertificateTagRepository giftCertificateTagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagService tagService, GiftCertificateTagRepository giftCertificateTagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.giftCertificateTagRepository = giftCertificateTagRepository;
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        giftCertificate.setCreatedDate(LocalDateTime.now());

        GiftCertificate createdCertificate = giftCertificateRepository.save(giftCertificate);

        List<Tag> tags = giftCertificate.getTags();
        if (!CollectionUtils.isEmpty(tags)) {
            updateTags(giftCertificate.getTags(), createdCertificate);
        }

        return createdCertificate;
    }


    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll();
        giftCertificates.forEach(this::addTags);
        return giftCertificates;
    }

    @Override
    public GiftCertificate findById(Long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(id);

        if (certificateOptional.isPresent()) {
            GiftCertificate certificate = certificateOptional.get();
            addTags(certificate);
            return certificate;
        }

        throw new GiftCertificateNotFoundException("Gift Certificate Not found with id: " + id);
    }

    @Override
    @Transactional
    public GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate) {
        GiftCertificate existingGiftCertificate = giftCertificateRepository
                .findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException("Gift Certificate Not found with id: " + id));

        if (updatedGiftCertificate.getName() != null)
            existingGiftCertificate.setName(updatedGiftCertificate.getName());

        if (updatedGiftCertificate.getDescription() != null)
            existingGiftCertificate.setDescription(updatedGiftCertificate.getDescription());

        if (updatedGiftCertificate.getPrice() != null)
            existingGiftCertificate.setPrice(updatedGiftCertificate.getPrice());

        if (updatedGiftCertificate.getDuration() != null)
            existingGiftCertificate.setDuration(updatedGiftCertificate.getDuration());

        existingGiftCertificate.setLastUpdatedDate(LocalDateTime.now());

        List<Tag> tags = updatedGiftCertificate.getTags();
        giftCertificateRepository.update(existingGiftCertificate);
        updateTags(tags, existingGiftCertificate);

        return existingGiftCertificate;
    }

    @Override
    public void delete(Long id) {
        giftCertificateRepository.delete(id);
    }

    @Override
    public List<GiftCertificate> findCertificatesByCriteria(String tagName, String search, String sortBy, boolean ascending) {
        List<GiftCertificate> certificates = giftCertificateRepository.findCertificatesByCriteria(tagName, search, sortBy, ascending);
        certificates.forEach(this::addTags);
        return certificates;
    }

    private void updateTags(List<Tag> updatedTags, GiftCertificate certificate) {
        for (Tag tag : updatedTags) {
            Optional<Tag> existingTag = tagService.findTagByName(tag.getName());
            Tag savedTag = existingTag.orElseGet(() -> tagService.create(tag));
            tag.setId(savedTag.getId());
            giftCertificateTagRepository.save(new GiftCertificateTag(certificate.getId(), tag.getId()));
        }
        certificate.setTags(updatedTags);
    }

    private void addTags(GiftCertificate certificate) {
        List<GiftCertificateTag> associations = giftCertificateTagRepository.findAssociationsByGiftCertificateId(certificate.getId());

        List<Tag> tags = associations.stream()
                .map(association -> tagService.findById(association.getTagId()))
                .collect(Collectors.toList());

        certificate.setTags(tags);
    }

}
