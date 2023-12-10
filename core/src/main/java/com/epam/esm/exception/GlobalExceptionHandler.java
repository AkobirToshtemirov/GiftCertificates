package com.epam.esm.exception;

import com.epam.esm.dto.ApiErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            NotFoundException.class,
            OperationException.class
    })
    public ResponseEntity<ApiErrorResponseDTO> handleApiException(RuntimeException ex) {
        HttpStatus status = resolveHttpStatus(ex);
        int errorCode = calculateErrorCode(status, ex);

        ApiErrorResponseDTO apiError = new ApiErrorResponseDTO(ex.getMessage(), errorCode, status);
        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponseDTO> handleException(Exception ex) {
        int errorCode = 50001;
        ApiErrorResponseDTO apiError = new ApiErrorResponseDTO("Internal Server Error: " + ex.getMessage(), errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    private HttpStatus resolveHttpStatus(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        return (responseStatus != null) ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private int calculateErrorCode(HttpStatus httpStatus, Exception ex) {
        int baseErrorCode;

        if (httpStatus == HttpStatus.NOT_FOUND)
            baseErrorCode = 40400;
        else if (httpStatus == HttpStatus.BAD_REQUEST)
            baseErrorCode = 40000;
        else
            baseErrorCode = 50000;

        if (ex instanceof NotFoundException)
            baseErrorCode += 1;
        else if (ex instanceof OperationException)
            baseErrorCode += 2;

        return baseErrorCode;
    }
}
