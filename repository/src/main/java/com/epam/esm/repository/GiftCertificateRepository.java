package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;

import java.util.List;

/**
 * Repository interface for CRUD operations on GiftCertificate entities.
 */
public interface GiftCertificateRepository extends BaseRepository<GiftCertificate> {

    /**
     * Updates the given GiftCertificate.
     *
     * @param giftCertificate the GiftCertificate to be updated
     * @return the updated GiftCertificate
     * @throws NotFoundException  if the GiftCertificate to be updated is not found
     * @throws OperationException if an error occurs during the update operation
     */
    GiftCertificate update(GiftCertificate giftCertificate) throws NotFoundException, OperationException;

    /**
     * Retrieves a list of GiftCertificates based on specified criteria.
     *
     * @param tagNames  the list of tag names to filter by
     * @param search    the search string to filter by
     * @param sortBy    the field by which to sort the results
     * @param ascending indicates whether to sort in ascending or descending order
     * @return a list of GiftCertificates based on the specified criteria
     * @throws OperationException if an error occurs during the retrieval operation
     */
    List<GiftCertificate> findCertificatesByCriteria(List<String> tagNames, String search, String sortBy, boolean ascending) throws OperationException;
}
