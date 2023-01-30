package ru.yandex.tonychem.ewmmainservice.user.model.dto;

import lombok.Value;

@Value
public class UserDto {
    Long id;
    String name;
    String email;
}
