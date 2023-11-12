package ru.yandex.tonychem.ewmmainservice.compilation.service;

import org.springframework.http.ResponseEntity;

public interface PublicCompilationService {
    ResponseEntity<Object> compilationsBy(Boolean pinned, Integer from, Integer size);

    ResponseEntity<Object> compilationById(long compId);
}
