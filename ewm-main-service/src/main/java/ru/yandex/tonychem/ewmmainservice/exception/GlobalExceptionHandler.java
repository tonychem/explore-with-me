package ru.yandex.tonychem.ewmmainservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchUserException;

import java.time.Instant;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_FORMATTER;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ApiError> handleMissingEntityException(NoSuchUserException e) {
        return new ResponseEntity<>(new ApiError(null, "NOT_FOUND",
                "The required object was not found", e.getMessage(),
                DATETIME_FORMATTER.format(Instant.now())), HttpStatus.NOT_FOUND);
    }
}
