package com.epam.esm.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonRootName(value = "tag")
@Relation(collectionRelation = "tags")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagModel extends RepresentationModel<TagModel> {
    private Long id;
    private String name;
}
