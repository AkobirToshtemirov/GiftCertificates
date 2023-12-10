package com.epam.esm.dto;

import org.springframework.http.HttpStatus;

public record ApiErrorResponseDTO(String errorMessage, int errorCode, HttpStatus httpStatus) {
}
