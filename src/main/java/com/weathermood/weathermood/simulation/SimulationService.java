package com.weathermood.weathermood.simulation;

import com.weathermood.weathermood.contents.AnimeContentResponse;
import com.weathermood.weathermood.contents.Content;
import com.weathermood.weathermood.contents.ContentExternalService;
import com.weathermood.weathermood.contents.ContentRecommendationResponse;
import com.weathermood.weathermood.contents.ContentRepository;
import com.weathermood.weathermood.contents.DramaContentResponse;
import com.weathermood.weathermood.ending.Ending;
import com.weathermood.weathermood.ending.EndingRepository;
import com.weathermood.weathermood.route.RouteType;
import com.weathermood.weathermood.route.RouteTypeRepository;
import com.weathermood.weathermood.weather.WeatherType;
import com.weathermood.weathermood.weather.WeatherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;

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

    private final ContentExternalService contentExternalService;

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
        // 외부 API 추천만 사용
        // DB 샘플 fallback 사용 안 함
        return createExternalRecommendations(routeId, mainEmotion, weatherCode);
    }

   /* private RecommendationBundleResponse createRecommendations(
            Long routeId,
            String mainEmotion,
            String weatherCode
    ) {
        try {
            return createExternalRecommendations(routeId, mainEmotion, weatherCode);
        } catch (Exception e) {
            return createDbFallbackRecommendations(routeId, mainEmotion, weatherCode);
        }
    } */

    private RecommendationBundleResponse createExternalRecommendations(
            Long routeId,
            String mainEmotion,
            String weatherCode
    ) {
        String animeKeyword = getRandomAnimeKeyword(routeId, weatherCode);
        String dramaKeyword = getRandomDramaKeyword(routeId, weatherCode);

        Long reverseRouteId = findReverseRouteId(routeId);
        String reverseAnimeKeyword = getRandomAnimeKeyword(reverseRouteId, weatherCode);
        String reverseDramaKeyword = getRandomDramaKeyword(reverseRouteId, weatherCode);

        List<AnimeContentResponse> animeItems =
                contentExternalService.searchAnime(animeKeyword, 1).getItems();

        List<DramaContentResponse> dramaItems =
                contentExternalService.searchDramas(dramaKeyword, 1).getItems();

        List<AnimeContentResponse> reverseAnimeItems =
                contentExternalService.searchAnime(reverseAnimeKeyword, 1).getItems();

        List<DramaContentResponse> reverseDramaItems =
                contentExternalService.searchDramas(reverseDramaKeyword, 1).getItems();

        animeItems = shuffleList(animeItems);
        dramaItems = shuffleList(dramaItems);
        reverseAnimeItems = shuffleList(reverseAnimeItems);
        reverseDramaItems = shuffleList(reverseDramaItems);
        List<ContentRecommendationResponse> prescription = new ArrayList<>();

        animeItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .limit(2)
                .map(item -> ContentRecommendationResponse.fromAnime(
                        item,
                        93,
                        mainEmotion + " 감정과 현재 루트에 어울리는 애니 추천입니다."
                ))
                .forEach(prescription::add);

        dramaItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .limit(1)
                .map(item -> ContentRecommendationResponse.fromDrama(
                        item,
                        91,
                        mainEmotion + " 감정과 현재 루트에 어울리는 드라마 추천입니다."
                ))
                .forEach(prescription::add);

        List<ContentRecommendationResponse> reverse = new ArrayList<>();

        reverseAnimeItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .limit(1)
                .map(item -> ContentRecommendationResponse.fromAnime(
                        item,
                        84,
                        "현재 루트와 반대되는 분위기의 애니 추천입니다."
                ))
                .forEach(reverse::add);

        reverseDramaItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .limit(1)
                .map(item -> ContentRecommendationResponse.fromDrama(
                        item,
                        82,
                        "현재 루트와 반대되는 분위기의 드라마 추천입니다."
                ))
                .forEach(reverse::add);

        ContentRecommendationResponse setAnime = animeItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .findFirst()
                .map(item -> ContentRecommendationResponse.fromAnime(
                        item,
                        92,
                        "현재 루트에 어울리는 애니메이션 세트 추천입니다."
                ))
                .orElse(null);

        ContentRecommendationResponse setDrama = dramaItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .findFirst()
                .map(item -> ContentRecommendationResponse.fromDrama(
                        item,
                        90,
                        "현재 루트에 어울리는 드라마 세트 추천입니다."
                ))
                .orElse(null);

        if (prescription.isEmpty() && setAnime == null && setDrama == null) {
            throw new IllegalArgumentException("외부 API 추천 결과가 비어 있습니다.");
        }

        return new RecommendationBundleResponse(
                prescription,
                reverse,
                new RecommendationSetResponse(setAnime, setDrama)
        );
    }

    private RecommendationBundleResponse createDbFallbackRecommendations(
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

        return new RecommendationBundleResponse(
                prescription,
                reverse,
                new RecommendationSetResponse(anime, drama)
        );
    }

    private Long findReverseRouteId(Long routeId) {
        if (Objects.equals(routeId, 1L)) {
            return 4L;
        }

        if (Objects.equals(routeId, 4L)) {
            return 1L;
        }

        if (Objects.equals(routeId, 2L)) {
            return 3L;
        }

        if (Objects.equals(routeId, 3L)) {
            return 2L;
        }

        return 4L;
    }

    private String getRandomAnimeKeyword(Long routeId, String weatherCode) {
        List<String> keywords = new ArrayList<>();

        if (Objects.equals(routeId, 1L)) {
            keywords.addAll(List.of(
                    "Violet Evergarden",
                    "Garden of Words",
                    "Your Lie in April",
                    "A Silent Voice",
                    "Anohana",
                    "March Comes in Like a Lion"
            ));

            if ("RAIN".equals(weatherCode) || "CLOUDS".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "Garden of Words",
                        "Violet Evergarden"
                ));
            }

            if ("NIGHT".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "Your Lie in April",
                        "March Comes in Like a Lion"
                ));
            }
        }

        if (Objects.equals(routeId, 2L)) {
            keywords.addAll(List.of(
                    "Spirited Away",
                    "Howl's Moving Castle",
                    "Made in Abyss",
                    "Mushoku Tensei",
                    "fantasy adventure",
                    "isekai"
            ));

            if ("CLEAR".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "adventure",
                        "fantasy"
                ));
            }
        }

        if (Objects.equals(routeId, 3L)) {
            keywords.addAll(List.of(
                    "Steins Gate",
                    "Death Note",
                    "Monster",
                    "Psycho Pass",
                    "mystery",
                    "thriller"
            ));

            if ("RAIN".equals(weatherCode) || "CLOUDS".equals(weatherCode) || "NIGHT".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "mystery",
                        "psychological",
                        "suspense"
                ));
            }
        }

        if (Objects.equals(routeId, 4L)) {
            keywords.addAll(List.of(
                    "Spy x Family",
                    "Kaguya-sama",
                    "Gintama",
                    "Nichijou",
                    "Barakamon",
                    "comedy"
            ));

            if ("CLEAR".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "comedy",
                        "slice of life"
                ));
            }
        }

        if (keywords.isEmpty()) {
            keywords.add("Violet Evergarden");
        }

        return pickRandomKeyword(keywords);
    }

    private String getRandomDramaKeyword(Long routeId, String weatherCode) {
        List<String> keywords = new ArrayList<>();

        if (Objects.equals(routeId, 1L)) {
            keywords.addAll(List.of(
                    "나의 아저씨",
                    "눈이 부시게",
                    "멜로가 체질",
                    "괜찮아 사랑이야",
                    "슬기로운 의사생활",
                    "우리들의 블루스"
            ));

            if ("RAIN".equals(weatherCode) || "CLOUDS".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "나의 아저씨",
                        "눈이 부시게",
                        "우리들의 블루스"
                ));
            }
        }

        if (Objects.equals(routeId, 2L)) {
            keywords.addAll(List.of(
                    "환혼",
                    "호텔 델루나",
                    "도깨비",
                    "왕좌의 게임",
                    "fantasy",
                    "adventure"
            ));
        }

        if (Objects.equals(routeId, 3L)) {
            keywords.addAll(List.of(
                    "시그널",
                    "비밀의 숲",
                    "괴물",
                    "마우스",
                    "crime",
                    "mystery"
            ));

            if ("RAIN".equals(weatherCode) || "CLOUDS".equals(weatherCode) || "NIGHT".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "시그널",
                        "비밀의 숲",
                        "mystery"
                ));
            }
        }

        if (Objects.equals(routeId, 4L)) {
            keywords.addAll(List.of(
                    "으라차차 와이키키",
                    "김과장",
                    "멜로가 체질",
                    "쌈 마이웨이",
                    "comedy",
                    "sitcom"
            ));

            if ("CLEAR".equals(weatherCode)) {
                keywords.addAll(List.of(
                        "comedy",
                        "으라차차 와이키키"
                ));
            }
        }

        if (keywords.isEmpty()) {
            keywords.add("나의 아저씨");
        }

        return pickRandomKeyword(keywords);
    }

    private String pickRandomKeyword(List<String> keywords) {
        Collections.shuffle(keywords);
        return keywords.get(0);
    }

    private <T> List<T> shuffleList(List<T> items) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> copied = new ArrayList<>(items);
        Collections.shuffle(copied);
        return copied;
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
// 시뮬레이션 후 결과 나올시 완전한 ai 추천 알고리즘은 아님. 추후. 난이도 매우 어려움,
// 다만 api 사용을 한건 맞음. 키워드에 따라 외부 api에 작품 후보를 받아서 추천.
//db 샘플 데이터 아님.