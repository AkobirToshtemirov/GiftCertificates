package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
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
public class TagModelAssembler implements RepresentationModelAssembler<Tag, EntityModel<Tag>> {
    @Override
    public EntityModel<Tag> toModel(Tag entity) {
        Link selfLink = linkTo(methodOn(TagController.class).getTag(entity.getId())).withSelfRel();
        Link tagsLink = linkTo(methodOn(TagController.class).getTagsWithPage(1, 10)).withRel("tags");
        return EntityModel.of(entity, selfLink, tagsLink);
    }

    @Override
    public CollectionModel<EntityModel<Tag>> toCollectionModel(Iterable<? extends Tag> entities) {
        List<EntityModel<Tag>> entityModels = new ArrayList<>();
        entities.forEach(tag -> entityModels.add(toModel(tag)));
        Link tagsLink = linkTo(methodOn(TagController.class).getTagsWithPage(1, 10)).withSelfRel();
        return CollectionModel.of(entityModels, tagsLink);
    }

    public CollectionModel<EntityModel<Tag>> toCollectionModelNoPage(Iterable<? extends Tag> entities) {
        List<EntityModel<Tag>> entityModels = new ArrayList<>();
        entities.forEach(tag -> entityModels.add(toModel(tag)));
        Link tagsLink = linkTo(methodOn(TagController.class).getTags()).withSelfRel();
        return CollectionModel.of(entityModels, tagsLink);
    }
}
