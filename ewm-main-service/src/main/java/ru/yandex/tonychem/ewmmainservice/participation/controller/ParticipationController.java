package ru.yandex.tonychem.ewmmainservice.participation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.participation.service.ParticipationService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ParticipationController {
    private final ParticipationService participationService;

    @GetMapping("/{userId}/requests")
    public ResponseEntity<Object> getUserParticipationRequests(@PathVariable long userId) {
        return participationService.userParticipationRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> sendParticipationRequest(@PathVariable long userId,
                                                            @RequestParam long eventId) {
        return participationService.publishRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelParticipation(@PathVariable long userId,
                                                       @PathVariable long requestId) {
        return participationService.cancel(userId, requestId);
    }
}
