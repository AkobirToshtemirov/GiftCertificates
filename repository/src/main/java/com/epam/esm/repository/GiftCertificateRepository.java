package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {
    GiftCertificate save(GiftCertificate giftCertificate) throws OperationException;

    Optional<GiftCertificate> findById(Long id) throws NotFoundException;

    List<GiftCertificate> findAll() throws NotFoundException;

    void update(GiftCertificate giftCertificate) throws NotFoundException, OperationException;

    void delete(Long id) throws OperationException;

    List<GiftCertificate> findCertificatesByCriteria(String tagName, String search, String sortBy, boolean ascending) throws OperationException;
}
