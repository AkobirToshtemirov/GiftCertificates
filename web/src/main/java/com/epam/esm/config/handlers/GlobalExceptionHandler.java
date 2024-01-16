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

/**
 * GlobalExceptionHandler is a class that provides centralized exception handling for RESTful controllers in the application.
 * It includes methods to handle specific exceptions and build standardized error responses.
 *
 * @RestControllerAdvice Indicates that this class is designed to be used with annotated controllers
 * to handle exceptions across the entire application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the NotFoundException by building an error response with a 404 status code.
     *
     * @param e   The NotFoundException containing details about the exception.
     * @param req The HttpServletRequest associated with the request.
     * @return A ResponseEntity with the appropriate ErrorResponseDTO containing error details.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> error404(NotFoundException e, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    /**
     * Handles AuthException, OperationException, and ValidationException by building a general error response
     * with a 400 status code. These exceptions are grouped together as they share the same error response structure.
     *
     * @param e   The Exception containing details about the exception.
     * @param req The HttpServletRequest associated with the request.
     * @return A ResponseEntity with the appropriate ErrorResponseDTO containing error details.
     */
    @ExceptionHandler({AuthException.class, OperationException.class, ValidationException.class})
    public ResponseEntity<ErrorResponseDTO> generalExceptionHandler(Exception e, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }

    /**
     * Handles MethodArgumentNotValidException by building an error response with a 400 status code.
     * Extracts field errors and their corresponding messages to include in the error response.
     *
     * @param e   The MethodArgumentNotValidException containing details about the exception.
     * @param req The HttpServletRequest associated with the request.
     * @return A ResponseEntity with the appropriate ErrorResponseDTO containing error details.
     */
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

    /**
     * Builds a standardized error response based on the provided HttpStatus, errorBody, and HttpServletRequest.
     *
     * @param status    The HttpStatus to be set in the error response.
     * @param errorBody The body of the error response, containing details about the error.
     * @param req       The HttpServletRequest associated with the request.
     * @return A ResponseEntity with the appropriate ErrorResponseDTO containing error details.
     */
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