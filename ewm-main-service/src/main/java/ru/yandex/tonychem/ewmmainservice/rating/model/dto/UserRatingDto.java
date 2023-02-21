package ru.yandex.tonychem.ewmmainservice.rating.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.LikeStatus;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingDto {
    @NotNull
    private LikeStatus status;
}
