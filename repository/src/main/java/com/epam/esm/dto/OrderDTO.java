package com.epam.esm.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing an order.
 */
public record OrderDTO(
        @NotBlank Long userId,
        @NotBlank Long giftCertificateId
) implements Serializable {
}
