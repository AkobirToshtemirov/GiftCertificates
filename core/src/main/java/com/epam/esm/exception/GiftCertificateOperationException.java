package com.epam.esm.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GiftCertificateOperationException extends RuntimeException {

    public GiftCertificateOperationException(String message) {
        super(message);
    }

    public GiftCertificateOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
