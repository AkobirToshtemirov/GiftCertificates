package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler implements RepresentationModelAssembler<GiftCertificate, EntityModel<GiftCertificate>> {
    @Override
    public EntityModel<GiftCertificate> toModel(GiftCertificate entity) {
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(entity.getId())).withSelfRel();
        Link giftCertificatesLink = linkTo(methodOn(GiftCertificateController.class).getGiftCertificatesWithPage(1, 10)).withRel("giftCertificates");
        return EntityModel.of(entity, selfLink, giftCertificatesLink);
    }

    @Override
    public CollectionModel<EntityModel<GiftCertificate>> toCollectionModel(Iterable<? extends GiftCertificate> entities) {
        List<EntityModel<GiftCertificate>> entityModels = new ArrayList<>();
        entities.forEach(giftCertificate -> entityModels.add(toModel(giftCertificate)));
        Link giftCertificatesLink = linkTo(methodOn(GiftCertificateController.class).getGiftCertificatesWithPage(1, 10)).withSelfRel();
        return CollectionModel.of(entityModels, giftCertificatesLink);
    }

    public CollectionModel<EntityModel<GiftCertificate>> toCollectionModelNoPage(Iterable<? extends GiftCertificate> entities) {
        List<EntityModel<GiftCertificate>> entityModels = new ArrayList<>();
        entities.forEach(giftCertificate -> entityModels.add(toModel(giftCertificate)));
        Link giftCertificatesLink = linkTo(methodOn(GiftCertificateController.class).getGiftCertificates()).withSelfRel();
        return CollectionModel.of(entityModels, giftCertificatesLink);
    }
}
