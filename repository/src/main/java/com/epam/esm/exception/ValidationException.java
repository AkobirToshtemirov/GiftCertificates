package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing a validation error.
 * This exception is annotated with {@code @ResponseStatus(HttpStatus.BAD_REQUEST)}
 * to indicate that it should result in an HTTP 400 Bad Request status when thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }
}
