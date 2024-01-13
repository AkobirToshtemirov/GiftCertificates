package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonRootName(value = "user")
@Relation(collectionRelation = "users")
public class UserModel extends RepresentationModel<UserModel> {
    private Long id;
    private String username;
    private String email;
    private String password;
    private List<OrderModel> orderList;
}
