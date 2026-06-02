package com.weathermood.weathermood.weather;

import lombok.Getter;

import java.util.List;

@Getter
public class OpenWeatherResponse {

    private List<WeatherInfo> weather;
    private MainInfo main;
    private String name;

    @Getter
    public static class WeatherInfo {
        private String main;
        private String description;
    }

    @Getter
    public static class MainInfo {
        private Double temp;
    }
}
//api 응답 json 구조에 맞춘 dto, 날씨 메인 값,온도, 도시명 매핑해서 사용