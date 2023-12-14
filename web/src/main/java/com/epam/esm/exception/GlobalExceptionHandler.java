package com.epam.esm.exception;

import com.epam.esm.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.epam.esm.constants.ErrorCodeConstants.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleGiftCertificateNotFoundException(GiftCertificateNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, GIFT_CERTIFICATE_NOT_FOUND);
    }

    @ExceptionHandler(GiftCertificateOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleGiftCertificateOperationException(GiftCertificateOperationException ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, GIFT_CERTIFICATE_OPERATION_ERROR);
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleTagNotFoundException(TagNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, TAG_NOT_FOUND);
    }

    @ExceptionHandler(TagOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleTagOperationException(TagOperationException ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, TAG_OPERATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, OTHER_EXCEPTION);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(Exception ex, HttpStatus status, int errorCode) {
        ErrorResponseDTO apiError = new ErrorResponseDTO(ex.getMessage(), errorCode, status);
        return ResponseEntity.status(status).body(apiError);
    }
}
