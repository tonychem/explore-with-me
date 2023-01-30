package ru.yandex.tonychem.ewmmainservice.user.model.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class NewUserRequest {
    @NotBlank
    String name;
    @NotBlank
    String email;
}
