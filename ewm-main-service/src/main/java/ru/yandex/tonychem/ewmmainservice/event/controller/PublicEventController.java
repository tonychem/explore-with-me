package ru.yandex.tonychem.ewmmainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventSort;
import ru.yandex.tonychem.ewmmainservice.event.service.PublicEventService;
import statistics.client.StatisticsClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_FORMATTER;

@RestController
@RequestMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PublicEventController {

    private final static String SERVICE_NAME = "ewm-main-service";

    private final PublicEventService publicEventService;
    private final StatisticsClient statisticsClient;

    @GetMapping
    public ResponseEntity<Object> eventsBy(HttpServletRequest servletRequest,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                           @RequestParam(required = false) EventSort sort,
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

        statisticsClient.registerView(SERVICE_NAME, servletRequest.getRequestURI(), servletRequest.getRemoteAddr(),
                LocalDateTime.now());

        return publicEventService.eventsBy(text, categories, paid, start, end, onlyAvailable,
                sort, from, size);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> detailedInfoEvent(HttpServletRequest servletRequest,
                                                    @PathVariable long eventId) {
        statisticsClient.registerView(SERVICE_NAME, servletRequest.getRequestURI(), servletRequest.getRemoteAddr(),
                LocalDateTime.now());
        return publicEventService.detailedInfoEvent(eventId);
    }

}
