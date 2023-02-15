package ru.yandex.tonychem.ewmmainservice.rating.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.UserRatingDto;
import ru.yandex.tonychem.ewmmainservice.rating.service.RatingService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PrivateRatingController {

    private final RatingService ratingService;

    @PutMapping("/{userId}/rating/{eventId}")
    public ResponseEntity<Object> rateEvent(@PathVariable long userId, @PathVariable long eventId,
                                            @RequestBody @Valid UserRatingDto userRatingDto) {
        return ratingService.rateEvent(userId, eventId, userRatingDto);
    }

    @PatchMapping("/{userId}/rating/{eventId}")
    public ResponseEntity<Object> updateRating(@PathVariable long userId, @PathVariable long eventId,
                                               @RequestBody @Valid UserRatingDto userRatingDto) {
        return ratingService.updateRating(userId, eventId, userRatingDto);
    }

    @DeleteMapping("/{userId}/rating/{eventId}")
    public ResponseEntity<Void> removeRating(@PathVariable long userId, @PathVariable long eventId) {
        return ratingService.removeRating(userId, eventId);
    }

    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<Object> getRecommendations(@PathVariable long userId) {
        return ratingService.getRecommendations(userId);
    }
}
