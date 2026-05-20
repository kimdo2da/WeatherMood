package com.weathermood.weathermood.simulation;

import tools.jackson.databind.ObjectMapper;
import com.weathermood.weathermood.contents.Content;
import com.weathermood.weathermood.contents.ContentRecommendationResponse;
import com.weathermood.weathermood.contents.ContentRepository;
import com.weathermood.weathermood.ending.Ending;
import com.weathermood.weathermood.ending.EndingRepository;
import com.weathermood.weathermood.route.RouteType;
import com.weathermood.weathermood.route.RouteTypeRepository;
import com.weathermood.weathermood.weather.WeatherType;
import com.weathermood.weathermood.weather.WeatherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimulationService {

    private final SimulationQuestionRepository simulationQuestionRepository;
    private final SimulationChoiceRepository simulationChoiceRepository;
    private final SimulationResultRepository simulationResultRepository;

    private final WeatherTypeRepository weatherTypeRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final EndingRepository endingRepository;
    private final ContentRepository contentRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<SimulationQuestionResponse> getQuestions() {
        List<SimulationQuestion> questions =
                simulationQuestionRepository.findByIsActiveOrderByQuestionOrderAsc((byte) 1);

        return questions.stream()
                .map(SimulationQuestionResponse::from)
                .toList();
    }

    @Transactional
    public SimulationSubmitResponse submit(Long userId, SimulationSubmitRequest request) {
        validateSubmitRequest(request);

        WeatherType weatherType = weatherTypeRepository.findByWeatherCode(request.getWeatherCode())
                .orElseThrow(() -> new IllegalArgumentException("날씨 타입을 찾을 수 없습니다."));

        List<SimulationChoice> selectedChoices =
                simulationChoiceRepository.findByChoiceIdIn(request.getSelectedChoiceIds());

        if (selectedChoices.size() != request.getSelectedChoiceIds().size()) {
            throw new IllegalArgumentException("존재하지 않는 선택지가 포함되어 있습니다.");
        }

        Map<Long, Integer> routeScoreMap = calculateRouteScores(selectedChoices);
        Map<String, Integer> emotionScoreMap = calculateEmotionScores(selectedChoices);

        Long finalRouteId = findHighestRouteId(routeScoreMap);
        Integer totalScore = routeScoreMap.get(finalRouteId);
        String mainEmotion = findHighestEmotion(emotionScoreMap);

        RouteType routeType = routeTypeRepository.findById(finalRouteId)
                .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

        Ending ending = findEnding(finalRouteId, weatherType.getWeatherId());

        RecommendationBundleResponse recommendations =
                createRecommendations(finalRouteId, mainEmotion, weatherType.getWeatherCode());

        String recommendationsJson = toJson(recommendations);

        SimulationResult savedResult = simulationResultRepository.save(
                new SimulationResult(
                        mainEmotion,
                        totalScore,
                        request.getWeatherText(),
                        request.getTemperature(),
                        recommendationsJson,
                        userId,
                        weatherType.getWeatherId(),
                        finalRouteId,
                        ending.getEndingId()
                )
        );

        return new SimulationSubmitResponse(
                savedResult.getResultId(),
                new SimulationSubmitResponse.WeatherResult(
                        weatherType.getWeatherCode(),
                        weatherType.getWeatherName(),
                        request.getWeatherText(),
                        request.getTemperature()
                ),
                new SimulationSubmitResponse.RouteResult(
                        routeType.getRouteId(),
                        routeType.getRouteName(),
                        totalScore
                ),
                mainEmotion,
                new SimulationSubmitResponse.EndingResult(
                        ending.getEndingId(),
                        ending.getEndingName(),
                        ending.getDescription()
                ),
                recommendations
        );
    }

    private void validateSubmitRequest(SimulationSubmitRequest request) {
        if (request.getWeatherCode() == null || request.getWeatherCode().isBlank()) {
            throw new IllegalArgumentException("날씨 코드는 필수입니다.");
        }

        if (request.getSelectedChoiceIds() == null || request.getSelectedChoiceIds().isEmpty()) {
            throw new IllegalArgumentException("선택지는 최소 1개 이상 필요합니다.");
        }
    }

    private Map<Long, Integer> calculateRouteScores(List<SimulationChoice> choices) {
        Map<Long, Integer> routeScoreMap = new HashMap<>();

        for (SimulationChoice choice : choices) {
            Long routeId = choice.getRouteId();
            Integer score = choice.getRouteScore();

            routeScoreMap.put(
                    routeId,
                    routeScoreMap.getOrDefault(routeId, 0) + score
            );
        }

        return routeScoreMap;
    }

    private Map<String, Integer> calculateEmotionScores(List<SimulationChoice> choices) {
        Map<String, Integer> emotionScoreMap = new HashMap<>();

        for (SimulationChoice choice : choices) {
            String emotionName = choice.getEmotionName();
            Integer score = choice.getEmotionScore();

            emotionScoreMap.put(
                    emotionName,
                    emotionScoreMap.getOrDefault(emotionName, 0) + score
            );
        }

        return emotionScoreMap;
    }

    private Long findHighestRouteId(Map<Long, Integer> routeScoreMap) {
        return routeScoreMap.entrySet()
                .stream()
                .max(
                        Comparator
                                .comparing(Map.Entry<Long, Integer>::getValue)
                                .thenComparing(Map.Entry::getKey)
                )
                .orElseThrow(() -> new IllegalArgumentException("루트 점수를 계산할 수 없습니다."))
                .getKey();
    }

    private String findHighestEmotion(Map<String, Integer> emotionScoreMap) {
        return emotionScoreMap.entrySet()
                .stream()
                .max(
                        Comparator
                                .comparing(Map.Entry<String, Integer>::getValue)
                                .thenComparing(Map.Entry::getKey)
                )
                .orElseThrow(() -> new IllegalArgumentException("대표 감정을 계산할 수 없습니다."))
                .getKey();
    }

    private Ending findEnding(Long routeId, Long weatherId) {
        return endingRepository.findFirstByRouteIdAndWeatherId(routeId, weatherId)
                .or(() -> endingRepository.findFirstByRouteIdAndWeatherIdIsNull(routeId))
                .orElseThrow(() -> new IllegalArgumentException("조건에 맞는 엔딩을 찾을 수 없습니다."));
    }

    private RecommendationBundleResponse createRecommendations(
            Long routeId,
            String mainEmotion,
            String weatherCode
    ) {
        List<Content> prescriptionContents = contentRepository.findByRouteId(routeId)
                .stream()
                .limit(3)
                .toList();

        List<ContentRecommendationResponse> prescription = prescriptionContents.stream()
                .map(content -> ContentRecommendationResponse.of(
                        content,
                        90,
                        mainEmotion + " 감정과 현재 날씨 분위기에 어울리는 추천 작품입니다."
                ))
                .toList();

        Long reverseRouteId = findReverseRouteId(routeId);

        List<ContentRecommendationResponse> reverse = contentRepository.findByRouteId(reverseRouteId)
                .stream()
                .limit(2)
                .map(content -> ContentRecommendationResponse.of(
                        content,
                        82,
                        "현재 루트와 반대되는 분위기로 기분을 전환하기 좋은 작품입니다."
                ))
                .toList();

        ContentRecommendationResponse anime = contentRepository.findByRouteIdAndContentType(routeId, "ANIME")
                .stream()
                .findFirst()
                .map(content -> ContentRecommendationResponse.of(
                        content,
                        92,
                        "현재 루트에 어울리는 애니메이션 세트 추천입니다."
                ))
                .orElse(null);

        ContentRecommendationResponse drama = contentRepository.findByRouteIdAndContentType(routeId, "DRAMA")
                .stream()
                .findFirst()
                .map(content -> ContentRecommendationResponse.of(
                        content,
                        90,
                        "현재 루트에 어울리는 드라마 세트 추천입니다."
                ))
                .orElse(null);

        RecommendationSetResponse set = new RecommendationSetResponse(anime, drama);

        return new RecommendationBundleResponse(
                prescription,
                reverse,
                set
        );
    }

    private Long findReverseRouteId(Long routeId) {
        if (routeId == 1L) {
            return 4L;
        }

        if (routeId == 4L) {
            return 1L;
        }

        if (routeId == 2L) {
            return 3L;
        }

        if (routeId == 3L) {
            return 2L;
        }

        return 4L;
    }

    private String toJson(RecommendationBundleResponse recommendations) {
        try {
            return objectMapper.writeValueAsString(recommendations);
        } catch (Exception e) {
            throw new IllegalArgumentException("추천 콘텐츠 저장 JSON 변환에 실패했습니다.");
        }
    }
}
//선택지 ID 제출 성공
//JWT 사용자 인증 성공
//route 점수 계산 성공
//mainEmotion 계산 성공
//weatherCode → weather_types 매칭 성공
//route + weather 기준 ending 결정 성공
//contents 기반 추천 성공
//prescription / reverse / set 추천 구성 성공
//simulation_results 저장 성공
//날씨 코드 루트명 엔딩명 잘 나옴.