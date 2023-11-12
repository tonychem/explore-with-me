package ru.yandex.tonychem.ewmmainservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private final List<String> errors;
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;
}
