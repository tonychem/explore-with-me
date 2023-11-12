package ru.yandex.tonychem.ewmmainservice.exception.exceptions;

public class RequestAlreadyExistsException extends RuntimeException {
    public RequestAlreadyExistsException(String message) {
        super(message);
    }
}
