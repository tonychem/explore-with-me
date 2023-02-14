package ru.yandex.tonychem.ewmmainservice.category.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotEmpty(message = "Field: name. Error: must not be blank.")
    private String name;
}
