package ru.yandex.tonychem.ewmmainservice.compilation.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewCompilationDto {
    private List<Integer> events;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean pinned = false;

    @NotEmpty(message = "Field: title must not be null or empty")
    private String title;
}
