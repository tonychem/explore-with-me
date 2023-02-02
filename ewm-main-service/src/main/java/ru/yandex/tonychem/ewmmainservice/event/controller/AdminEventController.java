package ru.yandex.tonychem.ewmmainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.UpdateEventAdminRequest;
import ru.yandex.tonychem.ewmmainservice.event.service.AdminEventService;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_FORMATTER;
import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_PATTERN;

@RestController
@RequestMapping(value = "/admin/events", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<Object> eventsBy(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    DATETIME_FORMATTER);
        }

        if (rangeEnd != null) {
            end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    DATETIME_FORMATTER);
        }

        return adminEventService.eventsBy(users, states, categories, start, end, from, size);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable long eventId,
                                              @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return adminEventService.updateEvent(eventId, updateEventAdminRequest);
    }
}
