package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;

import java.util.List;

public interface GiftCertificateRepository extends BaseRepository<GiftCertificate> {

    GiftCertificate update(GiftCertificate giftCertificate) throws NotFoundException, OperationException;

    List<GiftCertificate> findCertificatesByCriteria(List<String> tagNames, String search, String sortBy, boolean ascending) throws OperationException;
}
