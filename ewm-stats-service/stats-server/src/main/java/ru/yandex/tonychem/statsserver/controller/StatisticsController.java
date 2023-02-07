package ru.yandex.tonychem.statsserver.controller;

import dto.EndPointHitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.statsserver.service.StatisticsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class StatisticsController {
    private final StatisticsService statisticsService;
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @PostMapping("/hit")
    public ResponseEntity<Void> registerView(@RequestBody EndPointHitDto endPointHitDto) {
        return statisticsService.registerView(endPointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStatistics(@RequestParam String start,
                                                @RequestParam String end,
                                                @RequestParam(required = false) List<String> uris,
                                                @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8),
                DATETIME_FORMATTER);

        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8),
                DATETIME_FORMATTER);

        List<String> decodedURIs = null;

        if (uris != null) {
            decodedURIs = new ArrayList<>();

            for (String encodedUrl : uris) {
                decodedURIs.add(URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8));
            }
        }

        return statisticsService.getStatistics(startTime, endTime, decodedURIs, unique);
    }
}
