package com.epam.esm.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing a token request.
 */
public record TokenRequest(
        @NotBlank String username,
        @NotBlank String password
) implements Serializable {
}