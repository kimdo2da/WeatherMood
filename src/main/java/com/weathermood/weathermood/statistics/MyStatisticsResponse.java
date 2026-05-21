package com.weathermood.weathermood.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MyStatisticsResponse {

    private String nickname;
    private Long totalSimulationCount;
    private MostSelectedWeather mostSelectedWeather;
    private MostReachedRoute mostReachedRoute;
    private MostReachedEnding mostReachedEnding;
    private List<MainEmotionStat> mainEmotionStats;
    private List<RecentResult> recentResults;

    @Getter
    @AllArgsConstructor
    public static class MostSelectedWeather {
        private String weatherName;
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    public static class MostReachedRoute {
        private String routeName;
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    public static class MostReachedEnding {
        private String endingName;
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    public static class MainEmotionStat {
        private String emotion;
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    public static class RecentResult {
        private Long resultId;
        private String weatherName;
        private String routeName;
        private String endingName;
        private LocalDateTime createdAt;
    }
}