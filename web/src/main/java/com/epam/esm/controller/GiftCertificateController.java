package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    public ResponseEntity<GiftCertificate> createGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate createdCertificate = giftCertificateService.createGiftCertificate(giftCertificate);
        return new ResponseEntity<>(createdCertificate, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> findGiftCertificateById(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateService.findGiftCertificateById(id);
        return ResponseEntity.ok(certificate);
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificate>> findAllGiftCertificates() {
        List<GiftCertificate> certificates = giftCertificateService.findAllGiftCertificates();
        return ResponseEntity.ok(certificates);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificate> updateGiftCertificate(
            @PathVariable Long id,
            @RequestBody GiftCertificate giftCertificate) {
        GiftCertificate updatedCertificate = giftCertificateService.updateGiftCertificate(id, giftCertificate);
        return ResponseEntity.ok(updatedCertificate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {
        giftCertificateService.deleteGiftCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

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
