package com.epam.esm.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing user registration information.
 */
public record UserRegisterDTO(
        @NotBlank String email,
        @NotBlank String username,

        @NotBlank String password
) implements Serializable {
}
