package com.weathermood.weathermood.weather;

import com.weathermood.weathermood.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weather") //weather이 붙는걸 간편하게 사용
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService; //WeatherService가 사실 일함

    @GetMapping("/types")
    public ApiResponse<Map<String, List<WeatherTypeResponse>>> getWeatherTypes() {
        List<WeatherTypeResponse> weatherTypes = weatherService.getWeatherTypes();

        return ApiResponse.success(
                Map.of("items", weatherTypes),
                "OK"
        );
    }
    // db의 weather_types 테이블에 저장된 날씨 타입 목록을 가져옵니다

    @GetMapping("/current")
    public ApiResponse<CurrentWeatherResponse> getCurrentWeather() {
        CurrentWeatherResponse response = weatherService.getCurrentWeather();

        return ApiResponse.success(
                response,
                "현재 날씨 조회에 성공했습니다."
        );
    }
}
//사용하는 외부 api에서 현재 날씨를 가져오고 그 값을 우리 서비스의 날씨 코드로 변환후 프론트에 반환.