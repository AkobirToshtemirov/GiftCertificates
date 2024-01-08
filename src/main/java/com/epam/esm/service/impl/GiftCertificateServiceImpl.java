package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;

    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }


    @Override
    public GiftCertificate create(GiftCertificate entity) {
        entity.setCreatedDate(LocalDateTime.now());
        List<Tag> tags = entity.getTags();
        for (Tag tag : tags) {
            if (tag.getGiftCertificates() == null) {
                tag.setGiftCertificates(new HashSet<>());
            }
            tag.getGiftCertificates().add(entity);
        }
        return giftCertificateRepository.save(entity);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateRepository.findAll();
    }

    @Override
    public List<GiftCertificate> findAllWithPage(int page, int size) {
        return giftCertificateRepository.findAllWithPage(page, size);
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return giftCertificateRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        try {
            giftCertificateRepository.delete(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Gift Certificate not found with id: " + id);
        }
    }

    @Override
    public GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate) {
        if (giftCertificateRepository.findById(id).isEmpty())
            throw new NotFoundException("Gift Certificate not found with id: " + id);

        updatedGiftCertificate.setId(id);
        updatedGiftCertificate.setLastUpdatedDate(LocalDateTime.now());

        return giftCertificateRepository.update(updatedGiftCertificate);
    }

    @Override
    public List<GiftCertificate> findCertificatesByCriteria(List<String> tagNames, String search, String sortBy, boolean ascending) {
        return giftCertificateRepository.findCertificatesByCriteria(tagNames, search, sortBy, ascending);
    }

    @Override
    public GiftCertificate updateGiftCertificateDuration(Long id, Double duration) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gift Certificate not found with id: " + id));

        giftCertificate.setDuration(duration);
        giftCertificate.setLastUpdatedDate(LocalDateTime.now());

        return giftCertificateRepository.save(giftCertificate);
    }

    @Override
    public GiftCertificate updateGiftCertificatePrice(Long id, BigDecimal price) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gift Certificate not found with id: " + id));

        giftCertificate.setPrice(price);
        giftCertificate.setLastUpdatedDate(LocalDateTime.now());

        return giftCertificateRepository.save(giftCertificate);
    }
}
