package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing an authentication error.
 * This exception is annotated with {@code @ResponseStatus(HttpStatus.BAD_REQUEST)}
 * to indicate that it should result in an HTTP 400 Bad Request status when thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthException extends AuthenticationException {

    /**
     * Constructs a new AuthException with the specified detail message.
     *
     * @param message the detail message
     */
    public AuthException(String message) {
        super(message);
    }
}
