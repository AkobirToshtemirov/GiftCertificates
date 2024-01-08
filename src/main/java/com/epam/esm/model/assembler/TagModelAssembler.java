package com.epam.esm.model.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.TagModel;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler extends BaseAssembler<Tag, TagModel> {
    public TagModelAssembler() {
        super(TagController.class, TagModel.class);
    }

    @Override
    @SneakyThrows
    public TagModel toModel(Tag entity) {
        TagModel tagModel = instantiateModel(entity);

        tagModel.add(createSelfLink(entity.getId()),
                createToAllLink(),
                createPostLink(),
                createDeleteLink(entity.getId()));

        tagModel.setId(entity.getId());
        tagModel.setName(entity.getName());

        return tagModel;
    }

    @SneakyThrows
    public CollectionModel<TagModel> toCollectionModel(Iterable<? extends Tag> entities, int page, int size) {
        CollectionModel<TagModel> tagModels = super.toCollectionModel(entities);

        tagModels.add(linkTo(methodOn(TagController.class).getTagsWithPage(page, size)).withSelfRel());

        return tagModels;
    }

    @SneakyThrows
    public CollectionModel<TagModel> toCollectionModelNoPage(Iterable<? extends Tag> entities) {
        List<TagModel> models = new ArrayList<>();
        entities.forEach(tag -> models.add(toModel(tag)));

        CollectionModel<TagModel> collectionModel = CollectionModel.of(models);
        collectionModel.add(linkTo(TagController.class).withSelfRel());
        return collectionModel;
    }
}
