package ru.yandex.tonychem.ewmmainservice.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.*;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_FORMATTER;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {NoSuchUserException.class, NoSuchCategoryException.class, NoSuchEventException.class,
            NoSuchParticipationRequestException.class, NoSuchCompilationException.class})
    public ResponseEntity<ApiError> handleMissingEntityException(Exception e) {
        return new ResponseEntity<>(new ApiError(null, "NOT_FOUND",
                "The required object was not found", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> handleConstraintsViolations(RuntimeException e) {
        return new ResponseEntity<>(new ApiError(null, "CONFLICT",
                "Integrity constraint has been violated.", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();

        HttpStatus status;
        String statusDescription;

        if (result.hasFieldErrors("eventDate")) {
            status = HttpStatus.CONFLICT;
            statusDescription = "CONFLICT";
        } else {
            status = HttpStatus.BAD_REQUEST;
            statusDescription = "BAD_REQUEST";
        }

        if (result.getFieldErrorCount() > 1) {
            List<String> errorList = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new ApiError(errorList, statusDescription, "Incorrectly made request",
                    null, DATETIME_FORMATTER.format(Instant.now())), status);
        }
        return new ResponseEntity<>(new ApiError(null, statusDescription, "Incorrectly made request",
                result.getFieldError().getDefaultMessage(), DATETIME_FORMATTER.format(Instant.now())),
                status);
    }

    @ExceptionHandler(value = {EventCreationException.class, EventUpdateException.class,
            RequestAlreadyExistsException.class, IllegalParticipationRequestStateException.class,
            ParticipationRequestUpdateException.class, RatingException.class})
    public ResponseEntity<ApiError> handleInappropriateConditions(RuntimeException e) {
        return new ResponseEntity<>(new ApiError(null, "FORBIDDEN",
                "For the requested operation the conditions are not met.", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {EventAccessException.class, DateTimeParseException.class, NumberFormatException.class})
    public ResponseEntity<ApiError> handleIncorrectRequests(Exception e) {
        return new ResponseEntity<>(new ApiError(null, "BAD_REQUEST",
                "Incorrectly made request", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.BAD_REQUEST);
    }
}
