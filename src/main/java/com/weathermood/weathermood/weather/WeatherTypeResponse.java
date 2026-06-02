package com.weathermood.weathermood.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherTypeResponse {

    private Long weatherId;
    private String weatherCode;
    private String weatherName;
    private String description;

    public static WeatherTypeResponse from(WeatherType weatherType) {
        return new WeatherTypeResponse(
                weatherType.getWeatherId(),
                weatherType.getWeatherCode(),
                weatherType.getWeatherName(),
                weatherType.getDescription()
        );
    }
}
//디비 entity를 프론트 응답용으로 변환해주는 dto from()
//외부 날씨 API → 실시간 날씨 가져오기
//weather_types DB → 우리 서비스에서 사용할 날씨 코드/이름 기준표
//외부 API의 "Rain"을 우리 서비스 기준 코드인 "RAIN"으로 바꾸고,
//그 코드로 DB에서 한글 이름을 찾아오는 식.
