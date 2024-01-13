package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing a resource not found error.
 * This exception is annotated with {@code @ResponseStatus(HttpStatus.NOT_FOUND)}
 * to indicate that it should result in an HTTP 404 Not Found status when thrown.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {


    /**
     * Constructs a new NotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
