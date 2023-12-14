package com.epam.esm.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponseDTO(
        String errorMessage,
        int errorCode,
        HttpStatus httpStatus
) {
}
