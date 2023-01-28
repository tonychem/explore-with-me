package ru.yandex.tonychem.statsserver.controller;

import dto.EndPointHitDto;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.statsserver.service.StatisticsService;
import ru.yandex.tonychem.utils.GlobalConstantConfig;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public void registerView(@RequestBody EndPointHitDto endPointHitDto) {
        statisticsService.registerView(endPointHitDto);
    }

    //Параметры запроса содержат URLEncoded-строки дат (start и end) и список url-ов (uri-s).
    @GetMapping("/stats")
    public Collection<ViewStats> getStatistics(@RequestParam String start,
                                               @RequestParam String end,
                                               @RequestParam(required = false) String uris,
                                               @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8),
                GlobalConstantConfig.DEFAULT_FORMATTER);

        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8),
                GlobalConstantConfig.DEFAULT_FORMATTER);

        String decodedURIs = null;

        if (uris != null) {
            decodedURIs = URLDecoder.decode(uris, StandardCharsets.UTF_8);
        }

        return statisticsService.getStatistics(startTime, endTime, decodedURIs, unique);
    }
}