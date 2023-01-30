package ru.yandex.tonychem.statsserver.entity;

import dto.EndPointHitDto;

public class EndpointHitMapper {
    public static EndpointHit toEndPointHit(EndPointHitDto endPointHitDto) {
        EndpointHit hit = new EndpointHit();
        hit.setApp(endPointHitDto.getApp());
        hit.setUri(endPointHitDto.getUri());
        hit.setIp(endPointHitDto.getIp());
        hit.setTimestamp(endPointHitDto.getTimestamp());
        return hit;
    }
}
