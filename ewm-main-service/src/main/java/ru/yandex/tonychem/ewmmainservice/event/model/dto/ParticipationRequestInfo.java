package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import lombok.Value;

@Value
public class ParticipationRequestInfo {
    Long eventId;
    Integer confirmedRequests;
}
