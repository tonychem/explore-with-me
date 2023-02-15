package ru.yandex.tonychem.ewmmainservice.utils;

public class RatingProcessor {
    public static double calculateScore(Long likeCount, Long dislikeCount) {
        //confidence interval
        double confidence = 0.95;
        //hard-coded statistical param that can be calculated by given confidence
        double zScore = 1.96;

        long totalRatingCount = likeCount + dislikeCount;

        if (totalRatingCount == 0) {
            return 0.0;
        }

        double phat = 1.0 * likeCount / totalRatingCount;

        return (phat + Math.pow(zScore, 2) / (2 * totalRatingCount)
                - zScore * Math.sqrt((phat * (1 - phat) + Math.pow(zScore, 2) / (4 * totalRatingCount)) / totalRatingCount))
                / (1 + zScore * zScore / totalRatingCount);
    }
}
