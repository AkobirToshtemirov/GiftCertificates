package com.epam.esm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing an order.
 */
public record OrderDTO(
        @NotBlank
        @Positive
        Long userId,
        @NotBlank
        @Positive
        Long giftCertificateId
) implements Serializable {
}
