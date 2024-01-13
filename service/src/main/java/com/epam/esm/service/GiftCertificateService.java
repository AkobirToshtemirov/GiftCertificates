package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for CRUD operations on GiftCertificate entities.
 */
public interface GiftCertificateService extends BaseService<GiftCertificate> {
    /**
     * Updates the GiftCertificate with the specified ID.
     *
     * @param id                     the ID of the GiftCertificate to be updated
     * @param updatedGiftCertificate the updated GiftCertificate
     * @return the updated GiftCertificate
     */
    GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate);

    /**
     * Retrieves a list of GiftCertificates based on specified criteria.
     *
     * @param tagNames  the list of tag names to filter by
     * @param search    the search string to filter by
     * @param sortBy    the field by which to sort the results
     * @param ascending indicates whether to sort in ascending or descending order
     * @return a list of GiftCertificates based on the specified criteria
     */
    List<GiftCertificate> findCertificatesByCriteria(List<String> tagNames, String search, String sortBy, boolean ascending);

    /**
     * Updates the duration of a GiftCertificate with the specified ID.
     *
     * @param id       the ID of the GiftCertificate to be updated
     * @param duration the new duration value
     * @return the updated GiftCertificate
     */
    GiftCertificate updateGiftCertificateDuration(Long id, Double duration);

    /**
     * Updates the price of a GiftCertificate with the specified ID.
     *
     * @param id    the ID of the GiftCertificate to be updated
     * @param price the new price value
     * @return the updated GiftCertificate
     */
    GiftCertificate updateGiftCertificatePrice(Long id, BigDecimal price);
}
