package com.epam.esm.model.assembler;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpMethod;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public abstract class BaseAssembler<T, D extends RepresentationModel<?>> extends RepresentationModelAssemblerSupport<T, D> {
    private static final String GET_ALL = "get-all";
    private static final String POST_LINK = "create";
    private static final String UPDATE_LINK = "update";
    private static final String DELETE_LINK = "delete";

    public BaseAssembler(Class<?> controllerClass, Class<D> resourceType) {
        super(controllerClass, resourceType);
    }

    protected Link createSelfLink(Long id) {
        return linkTo(getControllerClass())
                .slash(id)
                .withSelfRel()
                .withType(String.valueOf(HttpMethod.GET));
    }

    protected Link createPostLink() {
        return linkTo(getControllerClass())
                .slash("")
                .withRel(POST_LINK)
                .withType(String.valueOf(HttpMethod.POST));
    }

    protected Link createPatchLink() {
        return linkTo(getControllerClass())
                .slash("")
                .withRel(UPDATE_LINK)
                .withType(String.valueOf(HttpMethod.PATCH));
    }

    protected Link createDeleteLink(Long id) {
        return linkTo(getControllerClass())
                .slash(id)
                .withRel(DELETE_LINK)
                .withType(String.valueOf(HttpMethod.DELETE));
    }

    protected Link createToAllLink() {
        return linkTo(getControllerClass())
                .slash("")
                .withRel(GET_ALL)
                .withType(String.valueOf(HttpMethod.GET));
    }
}