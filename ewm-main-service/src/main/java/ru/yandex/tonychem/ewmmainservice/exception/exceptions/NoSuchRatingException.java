package ru.yandex.tonychem.ewmmainservice.exception.exceptions;

public class NoSuchRatingException extends RuntimeException {
    public NoSuchRatingException(String message) {
        super(message);
    }
}
