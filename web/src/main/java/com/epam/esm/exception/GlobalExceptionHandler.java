package com.epam.esm.exception;

import com.epam.esm.dto.ApiErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.epam.esm.constants.ErrorCodeConstants.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponseDTO> handleNotFoundException(NotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, NOT_FOUND);
    }

    @ExceptionHandler(OperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseDTO> handleOperationException(OperationException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, OPERATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponseDTO> handleGeneralException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, OTHER_EXCEPTION);
    }

    private ResponseEntity<ApiErrorResponseDTO> buildErrorResponse(Exception ex, HttpStatus status, int errorCode) {
        ApiErrorResponseDTO apiError = new ApiErrorResponseDTO(ex.getMessage(), errorCode, status);
        return ResponseEntity.status(status).body(apiError);
    }
}
