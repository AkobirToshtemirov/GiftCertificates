package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate);
    List<GiftCertificate> findCertificatesByCriteria(String tagName, String search, String sortBy, boolean ascending);
}
