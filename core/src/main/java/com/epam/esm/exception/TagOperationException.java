package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TagOperationException extends RuntimeException {

    public TagOperationException(String message) {
        super(message);
    }

    public TagOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
