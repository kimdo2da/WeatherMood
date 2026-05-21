package com.weathermood.weathermood.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class RecentStatisticsResponse {

    private String period;
    private List<RouteStat> routeStats;
    private List<PopularEnding> popularEndings;
    private List<PopularContent> popularContents;
    private Map<String, String> trend;

    @Getter
    @AllArgsConstructor
    public static class RouteStat {
        private String routeName;
        private Integer count;
        private Integer percentage;
    }

    @Getter
    @AllArgsConstructor
    public static class PopularEnding {
        private Integer rank;
        private String endingName;
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    public static class PopularContent {
        private Integer rank;
        private String title;
        private Long count;
    }
}