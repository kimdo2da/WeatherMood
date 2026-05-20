package com.weathermood.weathermood.results;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResultDetailResponse {

    private Long resultId;
    private Long userId;
    private WeatherResult weather;
    private RouteResult route;
    private String mainEmotion;
    private Integer totalScore;
    private EndingResult ending;
    private Object recommendations;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class WeatherResult {
        private Long weatherId;
        private String weatherCode;
        private String weatherName;
        private String weatherText;
        private BigDecimal temperature;
    }

    @Getter
    @AllArgsConstructor
    public static class RouteResult {
        private Long routeId;
        private String routeName;
    }

    @Getter
    @AllArgsConstructor
    public static class EndingResult {
        private Long endingId;
        private String endingName;
        private String description;
    }
}
//recommendations object 둔 이유
// 설계를 simlation_results.recommended_contents에 json 문자열로 저장했기에
// map/list 형태로 읽어서 그대로 받기