package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/gift-certificates", produces = "application/json", consumes = "application/json")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;

    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateModelAssembler giftCertificateModelAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
    }

    @GetMapping(value = "/{id}")
    public EntityModel<GiftCertificate> getGiftCertificate(@PathVariable("id") Long id) {
        return giftCertificateModelAssembler.toModel(giftCertificateService.findById(id)
                .orElseThrow(() -> new NotFoundException("Gift Certificate not found with id: " + id)));
    }

    @GetMapping
    public CollectionModel<EntityModel<GiftCertificate>> getGiftCertificates() {
        List<GiftCertificate> giftCertificates = giftCertificateService.findAll();
        return giftCertificateModelAssembler.toCollectionModelNoPage(giftCertificates);
    }

    @GetMapping("/paged")
    public CollectionModel<EntityModel<GiftCertificate>> getGiftCertificatesWithPage(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                     @RequestParam(required = false, defaultValue = "10") int size) {
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllWithPage(page, size);
        CollectionModel<EntityModel<GiftCertificate>> collectionModel = giftCertificateModelAssembler.toCollectionModel(giftCertificates);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(giftCertificates.size()));

        return collectionModel;
    }

    @PostMapping()
    public EntityModel<GiftCertificate> saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate savedGiftCertificate = giftCertificateService.create(giftCertificate);
        return giftCertificateModelAssembler.toModel(savedGiftCertificate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public EntityModel<GiftCertificate> updateGiftCertificate(@PathVariable Long id, @RequestBody GiftCertificate updatedGiftCertificate) {
        GiftCertificate giftCertificate = giftCertificateService.update(id, updatedGiftCertificate);
        return giftCertificateModelAssembler.toModel(giftCertificate);
    }

    @GetMapping("/search")
    public CollectionModel<EntityModel<GiftCertificate>> findCertificatesByCriteria(
            @RequestParam(required = false) String[] tagNames,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "false") boolean ascending
    ) {
        List<GiftCertificate> giftCertificates = giftCertificateService.findCertificatesByCriteria(tagNames, search, sortBy, ascending);
        return giftCertificateModelAssembler.toCollectionModelNoPage(giftCertificates);
    }

    @PatchMapping("/{id}/duration")
    public EntityModel<GiftCertificate> updateGiftCertificateDuration(@PathVariable Long id, @RequestBody Double duration) {
        GiftCertificate giftCertificate = giftCertificateService.updateGiftCertificateDuration(id, duration);
        return giftCertificateModelAssembler.toModel(giftCertificate);
    }

    @PatchMapping("/{id}/price")
    public EntityModel<GiftCertificate> updateGiftCertificatePrice(@PathVariable Long id, @RequestBody BigDecimal price) {
        GiftCertificate giftCertificate = giftCertificateService.updateGiftCertificatePrice(id, price);
        return giftCertificateModelAssembler.toModel(giftCertificate);
    }
}
