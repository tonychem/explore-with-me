package ru.yandex.tonychem.ewmmainservice.compilation.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.compilation.service.PublicCompilationService;

@RestController
@RequestMapping(value = "/compilations", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public ResponseEntity<Object> getCompilationsBy(@RequestParam(required = false) Boolean pinned,
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        return publicCompilationService.compilationsBy(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable long compId) {
        return publicCompilationService.compilationById(compId);
    }
}
