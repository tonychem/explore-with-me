package ru.yandex.tonychem.ewmmainservice.rating;

import org.junit.jupiter.api.Test;
import ru.yandex.tonychem.ewmmainservice.utils.RatingProcessor;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RatingTest {

    @Test
    public void ratingShouldBeHigherForMoreLikedFilms() {
        long likeCountEvent1 = 100l;
        long likeCountEvent2 = 99l;
        long dislikeCount = 50l;

        assertTrue(RatingProcessor.calculateScore(likeCountEvent1, dislikeCount) >
                RatingProcessor.calculateScore(likeCountEvent2, dislikeCount));
    }
}
