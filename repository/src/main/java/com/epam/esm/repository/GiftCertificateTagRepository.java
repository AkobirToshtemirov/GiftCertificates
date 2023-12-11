package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateTagRepository {
    GiftCertificateTag save(GiftCertificateTag giftCertificateTag) throws OperationException;

    Optional<GiftCertificateTag> findById(Long id) throws NotFoundException;

    boolean hasAssociation(Long giftCertificateId, Long tagId);

    List<GiftCertificateTag> findAssociationsByGiftCertificateId(Long giftCertificateId) throws NotFoundException;
}
