package com.epam.esm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing an error response.
 */
@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    /**
     * The path where the error occurred.
     */
    String errorPath;

    /**
     * The error code associated with the error.
     */
    Integer errorCode;

    /**
     * The body of the error response.
     */
    Object errorBody;

    /**
     * The timestamp when the error occurred. Defaults to the current date and time.
     */
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
}
