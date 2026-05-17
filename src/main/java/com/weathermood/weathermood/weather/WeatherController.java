package com.weathermood.weathermood.weather;

import com.weathermood.weathermood.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/types")
    public ApiResponse<Map<String, List<WeatherTypeResponse>>> getWeatherTypes() {
        List<WeatherTypeResponse> weatherTypes = weatherService.getWeatherTypes();

        return ApiResponse.success(
                Map.of("items", weatherTypes),
                "OK"
        );
    }

    @GetMapping("/current")
    public ApiResponse<CurrentWeatherResponse> getCurrentWeather() {
        CurrentWeatherResponse response = weatherService.getCurrentWeather();

        return ApiResponse.success(
                response,
                "현재 날씨 조회에 성공했습니다."
        );
    }
}