package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.GiftCertificateOperationException;

import java.util.List;

public interface GiftCertificateTagRepository {
    GiftCertificateTag save(GiftCertificateTag giftCertificateTag) throws GiftCertificateOperationException;

    List<GiftCertificateTag> findAssociationsByGiftCertificateId(Long giftCertificateId) throws GiftCertificateNotFoundException;
}
