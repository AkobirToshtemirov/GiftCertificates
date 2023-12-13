package com.epam.esm.exception;

import com.epam.esm.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            GiftCertificateNotFoundException.class,
            GiftCertificateOperationException.class,
            TagNotFoundException.class,
            TagOperationException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleApiException(RuntimeException ex) {
        HttpStatus status = resolveHttpStatus(ex);
        int errorCode = calculateErrorCode(status, ex);

        ErrorResponseDTO apiError = new ErrorResponseDTO(ex.getMessage(), errorCode, status);
        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        int errorCode = 50001;
        ErrorResponseDTO apiError = new ErrorResponseDTO("Internal Server Error: " + ex.getMessage(), errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    private HttpStatus resolveHttpStatus(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        return (responseStatus != null) ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private int calculateErrorCode(HttpStatus httpStatus, Exception ex) {
        int baseErrorCode;
        if (httpStatus == HttpStatus.NOT_FOUND) {
            baseErrorCode = 40400;
        } else if (httpStatus == HttpStatus.BAD_REQUEST) {
            baseErrorCode = 40000;
        } else {
            baseErrorCode = 50000;
        }

        if (ex instanceof GiftCertificateNotFoundException || ex instanceof GiftCertificateOperationException) {
            return baseErrorCode + 1;
        } else if (ex instanceof TagNotFoundException || ex instanceof TagOperationException) {
            return baseErrorCode + 2;
        } else {
            return baseErrorCode;
        }
    }
}
