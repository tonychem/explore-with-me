package ru.yandex.tonychem.ewmmainservice.rating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.rating.service.RatingService;

@RestController
@RequestMapping(value = "/rating", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PublicRatingController {

    private final RatingService ratingService;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Object> getEventRating(@PathVariable long eventId) {
        return ratingService.getEventRating(eventId);
    }

    @GetMapping("/event/popular")
    public ResponseEntity<Object> getPopularEvents(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ratingService.getPopularEvents(from, size);
    }

    @GetMapping("/compilation/popular")
    public ResponseEntity<Object> getPopularCompilations(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ratingService.getPopularCompilations(from, size);
    }
}
