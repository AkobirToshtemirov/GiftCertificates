package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing an operation error.
 * This exception is annotated with {@code @ResponseStatus(HttpStatus.BAD_REQUEST)}
 * to indicate that it should result in an HTTP 400 Bad Request status when thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperationException extends RuntimeException {

    /**
     * Constructs a new OperationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public OperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
