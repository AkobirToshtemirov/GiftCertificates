package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;

import java.util.List;

public interface GiftCertificateTagRepository {
    GiftCertificateTag save(GiftCertificateTag giftCertificateTag) throws OperationException;

    List<GiftCertificateTag> findAssociationsByGiftCertificateId(Long giftCertificateId) throws NotFoundException;
}
