package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.GiftCertificateModel;
import com.epam.esm.model.assembler.GiftCertificateModelAssembler;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    public GiftCertificateModel getGiftCertificate(@PathVariable("id") Long id) {
        return giftCertificateModelAssembler.toModel(giftCertificateService.findById(id)
                .orElseThrow(() -> new NotFoundException("Gift Certificate not found with id: " + id)));
    }

    @GetMapping(value = "/paged")
    public CollectionModel<GiftCertificateModel> getGiftCertificatesWithPage(@RequestParam(required = false, defaultValue = "1", name = "page") int page,
                                                                             @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllWithPage(page, size);

        return giftCertificateModelAssembler.toCollectionModel(giftCertificates, page, size);
    }

    @PostMapping()
    public GiftCertificateModel saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate savedGiftCertificate = giftCertificateService.create(giftCertificate);
        return giftCertificateModelAssembler.toModel(savedGiftCertificate);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable("id") Long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    public GiftCertificateModel updateGiftCertificate(@PathVariable("id") Long id, @RequestBody GiftCertificate updatedGiftCertificate) {
        GiftCertificate giftCertificate = giftCertificateService.update(id, updatedGiftCertificate);
        return giftCertificateModelAssembler.toModel(giftCertificate);
    }

    @GetMapping(value = "/search")
    public CollectionModel<GiftCertificateModel> findCertificatesByCriteria(
            @RequestParam(value = "tag", required = false) List<String> tagNames,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "ascending", required = false, defaultValue = "false") boolean ascending
    ) {
        List<GiftCertificate> giftCertificates = giftCertificateService.findCertificatesByCriteria(tagNames, search, sortBy, ascending);
        return giftCertificateModelAssembler.toCollectionModelNoPage(giftCertificates);
    }

    @PatchMapping(value = "/{id}/duration")
    public GiftCertificateModel updateGiftCertificateDuration(@PathVariable("id") Long id, @RequestBody Map<String, Double> requestBody) {
        if (!requestBody.containsKey("duration"))
            throw new ValidationException("Request body should contain 'duration' field.");

        Double duration = requestBody.get("duration");
        GiftCertificate giftCertificate = giftCertificateService.updateGiftCertificateDuration(id, duration);
        return giftCertificateModelAssembler.toModel(giftCertificate);
    }

    @PatchMapping(value = "/{id}/price")
    public GiftCertificateModel updateGiftCertificatePrice(@PathVariable("id") Long id, @RequestBody Map<String, BigDecimal> requestBody) {
        if (!requestBody.containsKey("price"))
            throw new ValidationException("Request body should contain 'price' field.");

        BigDecimal price = requestBody.get("price");
        GiftCertificate giftCertificate = giftCertificateService.updateGiftCertificatePrice(id, price);
        return giftCertificateModelAssembler.toModel(giftCertificate);
    }
}
