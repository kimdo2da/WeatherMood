package com.weathermood.weathermood.simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimulationSubmitResponse {

    private Long resultId;
    private WeatherResult weather;
    private RouteResult route;
    private String mainEmotion;
    private EndingResult ending;
    private RecommendationBundleResponse recommendations;

    @Getter
    @AllArgsConstructor
    public static class WeatherResult {
        private String weatherCode;
        private String weatherName;
        private String weatherText;
        private java.math.BigDecimal temperature;
    }

    @Getter
    @AllArgsConstructor
    public static class RouteResult {
        private Long routeId;
        private String routeName;
        private Integer totalScore;
    }

    @Getter
    @AllArgsConstructor
    public static class EndingResult {
        private Long endingId;
        private String endingName;
        private String description;
    }
}