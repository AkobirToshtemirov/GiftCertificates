package com.epam.esm.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record UserRegisterDTO(
        @NotBlank String email,
        @NotBlank String username,

        @NotBlank String password
) implements Serializable {
}
