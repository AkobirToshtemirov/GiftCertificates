package com.epam.esm.config.handlers;

import com.epam.esm.dto.ErrorResponseDTO;
import com.epam.esm.exception.AuthException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;
import com.epam.esm.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> error404(NotFoundException e, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    @ExceptionHandler({AuthException.class, OperationException.class, ValidationException.class})
    public ResponseEntity<ErrorResponseDTO> generalExceptionHandler(Exception e, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> notValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, List<String>> errorBody = new HashMap<>();

        for (FieldError fieldError : e.getFieldErrors()) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            errorBody.compute(field, (s, strings) -> {
                strings = Objects.requireNonNullElse(strings, new ArrayList<>());
                strings.add(message);
                return strings;
            });
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorBody, req);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, Object errorBody, HttpServletRequest req) {
        return ResponseEntity
                .status(status)
                .body(ErrorResponseDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(status.value())
                        .errorBody(errorBody)
                        .build());
    }
}