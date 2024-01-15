package com.epam.esm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role name must not be blank")
    private String name;

    @NotBlank(message = "Role code must not be blank. Example: USER, ADMIN, MANAGER")
    private String code;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}