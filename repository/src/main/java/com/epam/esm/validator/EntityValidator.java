package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EntityValidator {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> void validateEntity(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation error(s) occurred for ");
            errorMessage.append(entity.getClass().getSimpleName()).append(" entity: ");
            for (ConstraintViolation<T> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(errorMessage.toString());
        }
    }
}
