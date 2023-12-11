package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificate createGiftCertificate(GiftCertificate giftCertificate);

    List<GiftCertificate> findAllGiftCertificates();

    GiftCertificate findGiftCertificateById(Long id);

    GiftCertificate updateGiftCertificate(Long id, GiftCertificate updatedGiftCertificate);

    void deleteGiftCertificate(Long id);

    List<GiftCertificate> findCertificatesByCriteria(String tagName, String search, String sortBy, boolean ascending);
}
