package ru.yandex.tonychem.ewmmainservice.exception.exceptions;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
