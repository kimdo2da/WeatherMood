package com.weathermood.weathermood.results;

import com.weathermood.weathermood.ending.Ending;
import com.weathermood.weathermood.ending.EndingRepository;
import com.weathermood.weathermood.route.RouteType;
import com.weathermood.weathermood.route.RouteTypeRepository;
import com.weathermood.weathermood.simulation.SimulationResult;
import com.weathermood.weathermood.simulation.SimulationResultRepository;
import com.weathermood.weathermood.weather.WeatherType;
import com.weathermood.weathermood.weather.WeatherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {

    private final SimulationResultRepository simulationResultRepository;
    private final WeatherTypeRepository weatherTypeRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final EndingRepository endingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResultDetailResponse getResultDetail(Long userId, Long resultId) {
        SimulationResult result = simulationResultRepository.findByResultIdAndUserId(resultId, userId)
                .orElseThrow(() -> new IllegalArgumentException("결과를 찾을 수 없습니다."));

        WeatherType weatherType = weatherTypeRepository.findById(result.getWeatherId())
                .orElseThrow(() -> new IllegalArgumentException("날씨 타입을 찾을 수 없습니다."));

        RouteType routeType = routeTypeRepository.findById(result.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

        Ending ending = endingRepository.findById(result.getEndingId())
                .orElseThrow(() -> new IllegalArgumentException("엔딩을 찾을 수 없습니다."));

        Object recommendations = parseRecommendations(result.getRecommendedContents());

        return new ResultDetailResponse(
                result.getResultId(),
                result.getUserId(),
                new ResultDetailResponse.WeatherResult(
                        weatherType.getWeatherId(),
                        weatherType.getWeatherCode(),
                        weatherType.getWeatherName(),
                        result.getWeatherText(),
                        result.getTemperature()
                ),
                new ResultDetailResponse.RouteResult(
                        routeType.getRouteId(),
                        routeType.getRouteName()
                ),
                result.getMainEmotion(),
                result.getTotalScore(),
                new ResultDetailResponse.EndingResult(
                        ending.getEndingId(),
                        ending.getEndingName(),
                        ending.getDescription()
                ),
                recommendations,
                result.getCreatedAt()
        );
    }

    public List<MyResultResponse> getMyResults(Long userId) {
        List<SimulationResult> results =
                simulationResultRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return results.stream()
                .map(this::toMyResultResponse)
                .toList();
    }
    //해당 사용자의 결과 조회

    private MyResultResponse toMyResultResponse(SimulationResult result) {
        WeatherType weatherType = weatherTypeRepository.findById(result.getWeatherId())
                .orElseThrow(() -> new IllegalArgumentException("날씨 타입을 찾을 수 없습니다."));

        RouteType routeType = routeTypeRepository.findById(result.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

        Ending ending = endingRepository.findById(result.getEndingId())
                .orElseThrow(() -> new IllegalArgumentException("엔딩을 찾을 수 없습니다."));

        return new MyResultResponse(
                result.getResultId(),
                weatherType.getWeatherName(),
                routeType.getRouteName(),
                ending.getEndingName(),
                result.getMainEmotion(),
                result.getTotalScore(),
                result.getCreatedAt()
        );
    }

    private Object parseRecommendations(String recommendedContents) {
        if (recommendedContents == null || recommendedContents.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(recommendedContents, Object.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("추천 콘텐츠 JSON을 읽는 데 실패했습니다.");
        }
    }
}