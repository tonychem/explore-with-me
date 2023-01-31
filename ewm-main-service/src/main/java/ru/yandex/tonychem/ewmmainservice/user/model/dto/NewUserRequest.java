package ru.yandex.tonychem.ewmmainservice.user.model.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class NewUserRequest {
    @NotBlank(message = "Field: name. Error: must not be blank.")
    String name;
    @NotBlank(message = "Field: email. Error: must not be blank.")
    @Email(message = "Invalid email")
    String email;
}
