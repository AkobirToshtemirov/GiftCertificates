package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.GiftCertificateOperationException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {
    GiftCertificate save(GiftCertificate giftCertificate) throws GiftCertificateOperationException;

    Optional<GiftCertificate> findById(Long id) throws GiftCertificateNotFoundException;

    List<GiftCertificate> findAll() throws GiftCertificateNotFoundException;

    void update(GiftCertificate giftCertificate) throws GiftCertificateNotFoundException, GiftCertificateOperationException;

    void delete(Long id) throws GiftCertificateOperationException;

    List<GiftCertificate> findCertificatesByCriteria(String tagName, String search, String sortBy, boolean ascending) throws GiftCertificateOperationException;
}
