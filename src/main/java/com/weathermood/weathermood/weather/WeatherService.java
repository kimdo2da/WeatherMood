package com.weathermood.weathermood.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherTypeRepository weatherTypeRepository;

    @Value("${weather.api.key}") //api키
    private String apiKey;

    @Value("${weather.api.city}")//도시
    private String city;

    public List<WeatherTypeResponse> getWeatherTypes() {
        return weatherTypeRepository.findAll()
                .stream()
                .map(WeatherTypeResponse::from)
                .toList();
    } //이거로 디비 호출(mysql) stream으로 변환 from으로 dto 변환

    public CurrentWeatherResponse getCurrentWeather() {
        OpenWeatherResponse openWeatherResponse = requestCurrentWeather();

        String openWeatherMain = getOpenWeatherMain(openWeatherResponse);
        String weatherCode = convertToWeatherCode(openWeatherMain);

        WeatherType weatherType = weatherTypeRepository.findByWeatherCode(weatherCode)
                .orElseThrow(() -> new IllegalArgumentException("날씨 타입을 찾을 수 없습니다."));

        String weatherText = getWeatherDescription(openWeatherResponse);
        double temperature = getTemperature(openWeatherResponse);
        String cityName = getCityName(openWeatherResponse);

        return new CurrentWeatherResponse(
                weatherType.getWeatherCode(),
                weatherType.getWeatherName(),
                weatherText,
                temperature,
                cityName
        );
    } //날씨 호출 CurrentWeatherResponse 리턴

    private OpenWeatherResponse requestCurrentWeather() {
        RestClient restClient = RestClient.create();

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.openweathermap.org")
                        .path("/data/2.5/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "en")
                        .build()
                )
                .retrieve()
                .body(OpenWeatherResponse.class);
    } //외부 api 호출 RestClient

    private String getOpenWeatherMain(OpenWeatherResponse response) {
        if (response == null || response.getWeather() == null || response.getWeather().isEmpty()) {
            throw new IllegalArgumentException("외부 날씨 API 응답이 올바르지 않습니다.");
        }

        return response.getWeather().get(0).getMain();
    }

    private String getWeatherDescription(OpenWeatherResponse response) {
        if (response == null || response.getWeather() == null || response.getWeather().isEmpty()) {
            return "";
        }

        return response.getWeather().get(0).getDescription();
    }

    private double getTemperature(OpenWeatherResponse response) {
        if (response == null || response.getMain() == null || response.getMain().getTemp() == null) {
            return 0.0;
        }

        return response.getMain().getTemp();
    }

    private String getCityName(OpenWeatherResponse response) {
        if (response == null || response.getName() == null || response.getName().isBlank()) {
            return city;
        }

        return response.getName();
    }

    private String convertToWeatherCode(String openWeatherMain) {
        if (openWeatherMain == null) {
            return "CLOUDS";
        }

        return switch (openWeatherMain) {
            case "Clear" -> "CLEAR";
            case "Rain", "Drizzle", "Thunderstorm" -> "RAIN";
            case "Clouds", "Mist", "Fog", "Haze", "Smoke", "Dust", "Sand", "Ash", "Squall", "Tornado" -> "CLOUDS";
            case "Snow" -> "SNOW";
            default -> "CLOUDS";
        };
    } 
} //우리식으로 외부 api의 날씨를 바꿔줌
//OpenWeatherMap 호출 → 실제 날씨 main/temp/name 추출
//→DB weather_types와 매칭 → 응답 반환