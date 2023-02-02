package ru.yandex.tonychem.ewmmainservice.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.EventUpdateException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchCategoryException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchEventException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchUserException;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_FORMATTER;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {NoSuchUserException.class, NoSuchCategoryException.class, NoSuchEventException.class})
    public ResponseEntity<ApiError> handleMissingEntityException(Exception e) {
        return new ResponseEntity<>(new ApiError(null, "NOT_FOUND",
                "The required object was not found", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>(new ApiError(null, "CONFLICT",
                "Integrity constraint has been violated.", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();

        if (result.getFieldErrorCount() > 1) {
            List<String> errorList = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(new ApiError(errorList, "BAD_REQUEST", "Incorrectly made request",
                    null, DATETIME_FORMATTER.format(Instant.now())), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiError(null, "BAD_REQUEST", "Incorrectly made request",
                result.getFieldError().getDefaultMessage(), DATETIME_FORMATTER.format(Instant.now())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ApiError> handleInappropriateConditions(EventUpdateException e) {
        return new ResponseEntity<>(new ApiError(null, "FORBIDDEN",
                "For the requested operation the conditions are not met.", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {DateTimeParseException.class, NumberFormatException.class})
    public ResponseEntity<ApiError> handleParsingExceptions(Exception e) {
        return new ResponseEntity<>(new ApiError(null, "BAD_REQUEST",
                "Incorrectly made request", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.BAD_REQUEST);
    }
}
