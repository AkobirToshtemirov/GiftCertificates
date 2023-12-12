package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateTagRepository giftCertificateTagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository, GiftCertificateTagRepository giftCertificateTagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateTagRepository = giftCertificateTagRepository;
    }

    @Override
    public GiftCertificate createGiftCertificate(GiftCertificate giftCertificate) {
        giftCertificate.setCreatedDate(LocalDateTime.now());
        giftCertificate.setLastUpdatedDate(LocalDateTime.now());

        GiftCertificate createdCertificate = giftCertificateRepository.save(giftCertificate);
        updateTags(giftCertificate.getTags(), createdCertificate);

        return createdCertificate;
    }


    @Override
    public List<GiftCertificate> findAllGiftCertificates() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll();
        giftCertificates.forEach(this::addTags);
        return giftCertificates;
    }

    @Override
    public GiftCertificate findGiftCertificateById(Long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(id);

        if (certificateOptional.isPresent()) {
            GiftCertificate certificate = certificateOptional.get();
            addTags(certificate);
            return certificate;
        }

        throw new GiftCertificateNotFoundException("Gift Certificate Not found with id: " + id);
    }

    @Override
    public GiftCertificate updateGiftCertificate(Long id, GiftCertificate updatedGiftCertificate) {
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
        updateTags(tags, existingGiftCertificate);

        giftCertificateRepository.update(existingGiftCertificate);

        return existingGiftCertificate;
    }

    @Override
    public void deleteGiftCertificate(Long id) {
        giftCertificateRepository.delete(id);
    }

    @Override
    public List<GiftCertificate> findCertificatesByCriteria(String tagName, String search, String sortBy, boolean ascending) {
        List<GiftCertificate> certificates = giftCertificateRepository.findCertificatesByCriteria(tagName, search, sortBy, ascending);
        certificates.forEach(this::addTags);
        return certificates;
    }

    private void updateTags(List<Tag> tags, GiftCertificate certificate) {
        for (Tag tag : tags) {
            Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());

            if (tagOptional.isPresent())
                tag.setId(tagOptional.get().getId());
            else
                tag.setId(tagRepository.save(tag).getId());

            if (!giftCertificateTagRepository.hasAssociation(certificate.getId(), tag.getId()))
                giftCertificateTagRepository.save(new GiftCertificateTag(certificate.getId(), tag.getId()));
        }
        certificate.setTags(tags);
    }

    private void addTags(GiftCertificate certificate) {
        List<GiftCertificateTag> associations = giftCertificateTagRepository.findAssociationsByGiftCertificateId(certificate.getId());

        List<Tag> tags = associations.stream()
                .map(association -> tagRepository.findById(association.getTagId()))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());

        certificate.setTags(tags);
    }

}
