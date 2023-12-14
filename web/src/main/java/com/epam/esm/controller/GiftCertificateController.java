package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.BaseService;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GiftCertificateController handles HTTP requests related to gift certificates, providing endpoints for creating,
 * retrieving, updating, and deleting gift certificates, as well as searching for gift certificates based on criteria.
 * <p>
 * This controller communicates with the {@link com.epam.esm.service.GiftCertificateService} for business logic
 * related to gift certificates.
 */
@RestController
@RequestMapping(
        path = "/gift-certificates",
        consumes = {"application/json"},
        produces = {"application/json"}
)
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final BaseService<GiftCertificate> giftCertificateBaseService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, BaseService<GiftCertificate> giftCertificateBaseService) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateBaseService = giftCertificateBaseService;
    }

    /**
     * Creates a new gift certificate.
     *
     * @param giftCertificate The gift certificate to be created.
     * @return ResponseEntity containing the created gift certificate and HTTP status code 201 (Created).
     */
    @PostMapping
    public ResponseEntity<GiftCertificate> createGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate createdCertificate = giftCertificateBaseService.create(giftCertificate);
        return new ResponseEntity<>(createdCertificate, HttpStatus.CREATED);
    }

    /**
     * Retrieves a gift certificate by its ID.
     *
     * @param id The ID of the gift certificate to be retrieved.
     * @return ResponseEntity containing the retrieved gift certificate and HTTP status code 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> findGiftCertificateById(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateBaseService.findById(id);
        return ResponseEntity.ok(certificate);
    }

    /**
     * Retrieves all gift certificates.
     *
     * @return ResponseEntity containing the list of all gift certificates and HTTP status code 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificate>> findAllGiftCertificates() {
        List<GiftCertificate> certificates = giftCertificateBaseService.findAll();
        return ResponseEntity.ok(certificates);
    }

    /**
     * Updates a gift certificate by its ID.
     *
     * @param id              The ID of the gift certificate to be updated.
     * @param giftCertificate The updated gift certificate data.
     * @return ResponseEntity containing the updated gift certificate and HTTP status code 200 (OK).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificate> updateGiftCertificate(
            @PathVariable Long id,
            @RequestBody GiftCertificate giftCertificate) {
        GiftCertificate updatedCertificate = giftCertificateService.update(id, giftCertificate);
        return ResponseEntity.ok(updatedCertificate);
    }

    /**
     * Deletes a gift certificate by its ID.
     *
     * @param id The ID of the gift certificate to be deleted.
     * @return ResponseEntity with HTTP status code 204 (No Content) indicating successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {
        giftCertificateBaseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Searches for gift certificates based on criteria.
     *
     * @param tagName   The tag name to filter gift certificates.
     * @param search    The search term to filter gift certificates.
     * @param sortBy    The field by which to sort the gift certificates.
     * @param ascending A boolean indicating whether to sort in ascending order (default is descending).
     * @return ResponseEntity containing the list of gift certificates matching the criteria and HTTP status code 200 (OK).
     */
    @GetMapping("/search")
    public ResponseEntity<List<GiftCertificate>> findCertificatesByCriteria(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {

        List<GiftCertificate> certificates = giftCertificateService.findCertificatesByCriteria(tagName, search, sortBy, ascending);

        return ResponseEntity.ok(certificates);
    }
}
