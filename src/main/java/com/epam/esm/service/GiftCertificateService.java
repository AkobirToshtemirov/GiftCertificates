package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.math.BigDecimal;
import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificate> {
    GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate);

    List<GiftCertificate> findCertificatesByCriteria(List<String> tagNames, String search, String sortBy, boolean ascending);

    GiftCertificate updateGiftCertificateDuration(Long id, Double duration);

    GiftCertificate updateGiftCertificatePrice(Long id, BigDecimal price);
}
