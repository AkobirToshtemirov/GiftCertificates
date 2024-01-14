package com.epam.esm.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record TokenRequest(
        @NotBlank String username,
        @NotBlank String password
) implements Serializable {
}