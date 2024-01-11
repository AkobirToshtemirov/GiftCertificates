package com.epam.esm.model.assembler;


import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.GiftCertificateModel;
import com.epam.esm.model.TagModel;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler extends BaseAssembler<GiftCertificate, GiftCertificateModel> {
    public GiftCertificateModelAssembler() {
        super(GiftCertificateController.class, GiftCertificateModel.class);
    }

    @Override
    @SneakyThrows
    public GiftCertificateModel toModel(GiftCertificate entity) {
        GiftCertificateModel giftCertificateModel = instantiateModel(entity);

        giftCertificateModel.add(createSelfLink(entity.getId()),
                createPatchLink(),
                createDeleteLink(entity.getId()),
                createPostLink(),
                createToAllLink());

        setModelAttributes(giftCertificateModel, entity);
        return giftCertificateModel;
    }


    @SneakyThrows
    public CollectionModel<GiftCertificateModel> toCollectionModel(Iterable<? extends GiftCertificate> entities, int page, int size) {
        CollectionModel<GiftCertificateModel> userModels = super.toCollectionModel(entities);
        userModels.add(linkTo(methodOn(GiftCertificateController.class)
                .getGiftCertificatesWithPage(page, size))
                .withSelfRel());
        return userModels;
    }

    private List<TagModel> toTagModel(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        return tags.stream()
                .map(this::mapToTagModel)
                .collect(Collectors.toList());

    }

    @SneakyThrows
    private TagModel mapToTagModel(Tag tag) {
        return createTagModel(tag)
                .add(linkTo(TagController.class, methodOn(TagController.class)
                        .getTag(tag.getId()))
                        .withSelfRel());
    }

    private TagModel createTagModel(Tag tag) {
        return TagModel.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    private void setModelAttributes(GiftCertificateModel model, GiftCertificate entity) {
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setDuration(entity.getDuration());
        model.setPrice(entity.getPrice());
        model.setCreatedDate(entity.getCreatedDate());

        if (entity.getLastUpdatedDate() != null) {
            model.setLastUpdatedDate(entity.getLastUpdatedDate());
        }

        model.setTags(toTagModel(entity.getTags()));
    }

    public CollectionModel<GiftCertificateModel> toCollectionModelNoPage(Iterable<? extends GiftCertificate> entities) {
        List<GiftCertificateModel> models = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<GiftCertificateModel> collectionModel = CollectionModel.of(models);
        collectionModel.add(linkTo(GiftCertificateController.class).withSelfRel());
        return collectionModel;
    }
}
