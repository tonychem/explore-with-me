package ru.yandex.tonychem.ewmmainservice.participation.model.mapper;

import ru.yandex.tonychem.ewmmainservice.participation.model.dto.ParticipationRequestDto;
import ru.yandex.tonychem.ewmmainservice.participation.model.entity.ParticipationRequest;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(participationRequest.getId(), participationRequest.getParticipant().getId(),
                participationRequest.getEvent().getId(), participationRequest.getStatus().toString(),
                participationRequest.getCreated());
    }
}
