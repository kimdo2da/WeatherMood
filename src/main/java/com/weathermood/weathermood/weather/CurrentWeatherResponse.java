package com.weathermood.weathermood.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentWeatherResponse {

    private String weatherCode;
    private String weatherName;
    private String weatherText;
    private double temperature;
    private String city;
}
//current 응답 dto용