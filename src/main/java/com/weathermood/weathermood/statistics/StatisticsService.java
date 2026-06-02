package com.weathermood.weathermood.statistics;

import com.weathermood.weathermood.ending.Ending;
import com.weathermood.weathermood.ending.EndingRepository;
import com.weathermood.weathermood.route.RouteType;
import com.weathermood.weathermood.route.RouteTypeRepository;
import com.weathermood.weathermood.simulation.SimulationResult;
import com.weathermood.weathermood.simulation.SimulationResultRepository;
import com.weathermood.weathermood.users.User;
import com.weathermood.weathermood.users.UserRepository;
import com.weathermood.weathermood.weather.WeatherType;
import com.weathermood.weathermood.weather.WeatherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final SimulationResultRepository simulationResultRepository;
    private final WeatherTypeRepository weatherTypeRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final EndingRepository endingRepository;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecentStatisticsResponse getRecentStatistics() {
        LocalDateTime since = LocalDateTime.now().minusHours(24);

        List<SimulationResult> results =
                simulationResultRepository.findByCreatedAtAfterOrderByCreatedAtDesc(since);

        List<RecentStatisticsResponse.RouteStat> routeStats = createRouteStats(results);
        List<RecentStatisticsResponse.PopularEnding> popularEndings = createPopularEndings(results);
        List<RecentStatisticsResponse.PopularContent> popularContents = createPopularContents(results);
        Map<String, String> trend = createTrend(routeStats);

        return new RecentStatisticsResponse(
                "24h",
                routeStats,
                popularEndings,
                popularContents,
                trend
        );
    }
//24시간 통계흐름
    public MyStatisticsResponse getMyStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<SimulationResult> myResults =
                simulationResultRepository.findByUserIdOrderByCreatedAtDesc(userId);

        MyStatisticsResponse.MostSelectedWeather mostWeather = createMostSelectedWeather(myResults);
        MyStatisticsResponse.MostReachedRoute mostRoute = createMostReachedRoute(myResults);
        MyStatisticsResponse.MostReachedEnding mostEnding = createMostReachedEnding(myResults);
        List<MyStatisticsResponse.MainEmotionStat> emotionStats = createEmotionStats(myResults);
        List<MyStatisticsResponse.RecentResult> recentResults = createRecentResults(userId);

        return new MyStatisticsResponse(
                user.getNickname(),
                (long) myResults.size(),
                mostWeather,
                mostRoute,
                mostEnding,
                emotionStats,
                recentResults
        );
    }
//개인 통계
    private List<RecentStatisticsResponse.RouteStat> createRouteStats(List<SimulationResult> results) {
        if (results.isEmpty()) {
            return Collections.emptyList();
        }

        int total = results.size();

        Map<Long, Long> routeCountMap = results.stream()
                .collect(Collectors.groupingBy(
                        SimulationResult::getRouteId,
                        Collectors.counting()
                ));
// 루트 결과 
        return routeCountMap.entrySet()
                .stream()
                .map(entry -> {
                    RouteType routeType = routeTypeRepository.findById(entry.getKey())
                            .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

                    int percentage = (int) Math.round((entry.getValue() * 100.0) / total);

                    return new RecentStatisticsResponse.RouteStat(
                            routeType.getRouteName(),
                            entry.getValue().intValue(),
                            percentage
                    );
                })
                .sorted(Comparator.comparing(RecentStatisticsResponse.RouteStat::getCount).reversed())
                .toList();
    } //퍼센트 계산

    private List<RecentStatisticsResponse.PopularEnding> createPopularEndings(List<SimulationResult> results) {
        Map<Long, Long> endingCountMap = results.stream()
                .collect(Collectors.groupingBy(
                        SimulationResult::getEndingId,
                        Collectors.counting()
                ));

        List<Map.Entry<Long, Long>> sortedEntries = endingCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        List<RecentStatisticsResponse.PopularEnding> response = new ArrayList<>();

        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<Long, Long> entry = sortedEntries.get(i);

            Ending ending = endingRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("엔딩을 찾을 수 없습니다."));

            response.add(new RecentStatisticsResponse.PopularEnding(
                    i + 1,
                    ending.getEndingName(),
                    entry.getValue()
            ));
        }

        return response;
    }
    //인기 엔딩 3

    private List<RecentStatisticsResponse.PopularContent> createPopularContents(List<SimulationResult> results) {
        Map<String, Long> titleCountMap = new HashMap<>();

        for (SimulationResult result : results) {
            List<String> titles = extractRecommendationTitles(result.getRecommendedContents());

            for (String title : titles) {
                titleCountMap.put(title, titleCountMap.getOrDefault(title, 0L) + 1);
            }
        }

        List<Map.Entry<String, Long>> sortedEntries = titleCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        List<RecentStatisticsResponse.PopularContent> response = new ArrayList<>();

        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Long> entry = sortedEntries.get(i);

            response.add(new RecentStatisticsResponse.PopularContent(
                    i + 1,
                    entry.getKey(),
                    entry.getValue()
            ));
        }

        return response;
    }
//인기 콘텐츠 3
    private List<String> extractRecommendationTitles(String recommendedContents) {
        if (recommendedContents == null || recommendedContents.isBlank()) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> root = objectMapper.readValue(recommendedContents, Map.class);

            List<String> titles = new ArrayList<>();

            Object prescriptionObj = root.get("prescription");
            if (prescriptionObj instanceof List<?> prescriptionList) {
                for (Object item : prescriptionList) {
                    if (item instanceof Map<?, ?> itemMap) {
                        Object title = itemMap.get("title");
                        if (title != null) {
                            titles.add(title.toString());
                        }
                    }
                }
            }

            Object reverseObj = root.get("reverse");
            if (reverseObj instanceof List<?> reverseList) {
                for (Object item : reverseList) {
                    if (item instanceof Map<?, ?> itemMap) {
                        Object title = itemMap.get("title");
                        if (title != null) {
                            titles.add(title.toString());
                        }
                    }
                }
            }

            Object setObj = root.get("set");
            if (setObj instanceof Map<?, ?> setMap) {
                Object animeObj = setMap.get("anime");
                if (animeObj instanceof Map<?, ?> animeMap) {
                    Object title = animeMap.get("title");
                    if (title != null) {
                        titles.add(title.toString());
                    }
                }

                Object dramaObj = setMap.get("drama");
                if (dramaObj instanceof Map<?, ?> dramaMap) {
                    Object title = dramaMap.get("title");
                    if (title != null) {
                        titles.add(title.toString());
                    }
                }
            }

            return titles;
        } catch (Exception e) {
            return Collections.emptyList();
        } //안 뜨면 반환 오류 x 빈 리스트
    }
// json 문자열에서 콘텐츠 제목만 뽑기
    private Map<String, String> createTrend(List<RecentStatisticsResponse.RouteStat> routeStats) {
        Map<String, String> trend = new LinkedHashMap<>();

        for (RecentStatisticsResponse.RouteStat stat : routeStats) {
            if (stat.getPercentage() >= 30) {
                trend.put(stat.getRouteName(), "UP");
            } else {
                trend.put(stat.getRouteName(), "DOWN");
            }
        }

        return trend;
    }
// 루트 트랜드 
    private MyStatisticsResponse.MostSelectedWeather createMostSelectedWeather(List<SimulationResult> results) {
        if (results.isEmpty()) {
            return null;
        }

        Map<Long, Long> weatherCountMap = results.stream()
                .collect(Collectors.groupingBy(
                        SimulationResult::getWeatherId,
                        Collectors.counting()
                ));

        Map.Entry<Long, Long> maxEntry = weatherCountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        WeatherType weatherType = weatherTypeRepository.findById(maxEntry.getKey())
                .orElseThrow(() -> new IllegalArgumentException("날씨 타입을 찾을 수 없습니다."));

        return new MyStatisticsResponse.MostSelectedWeather(
                weatherType.getWeatherName(),
                maxEntry.getValue()
        );
    }
// 내가 가장 많이 선택한 날씨
    private MyStatisticsResponse.MostReachedRoute createMostReachedRoute(List<SimulationResult> results) {
        if (results.isEmpty()) {
            return null;
        }

        Map<Long, Long> routeCountMap = results.stream()
                .collect(Collectors.groupingBy(
                        SimulationResult::getRouteId,
                        Collectors.counting()
                ));

        Map.Entry<Long, Long> maxEntry = routeCountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        RouteType routeType = routeTypeRepository.findById(maxEntry.getKey())
                .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

        return new MyStatisticsResponse.MostReachedRoute(
                routeType.getRouteName(),
                maxEntry.getValue()
        );
    }
// 내가 가장 많이 도달한 루트
    private MyStatisticsResponse.MostReachedEnding createMostReachedEnding(List<SimulationResult> results) {
        if (results.isEmpty()) {
            return null;
        }

        Map<Long, Long> endingCountMap = results.stream()
                .collect(Collectors.groupingBy(
                        SimulationResult::getEndingId,
                        Collectors.counting()
                ));

        Map.Entry<Long, Long> maxEntry = endingCountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        Ending ending = endingRepository.findById(maxEntry.getKey())
                .orElseThrow(() -> new IllegalArgumentException("엔딩을 찾을 수 없습니다."));

        return new MyStatisticsResponse.MostReachedEnding(
                ending.getEndingName(),
                maxEntry.getValue()
        );
    }
//내가 가장 많이 도달한 엔딩
    private List<MyStatisticsResponse.MainEmotionStat> createEmotionStats(List<SimulationResult> results) {
        Map<String, Long> emotionCountMap = results.stream()
                .collect(Collectors.groupingBy(
                        SimulationResult::getMainEmotion,
                        Collectors.counting()
                ));

        return emotionCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new MyStatisticsResponse.MainEmotionStat(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
    }

    private List<MyStatisticsResponse.RecentResult> createRecentResults(Long userId) {
        List<SimulationResult> recentResults =
                simulationResultRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);

        return recentResults.stream()
                .map(result -> {
                    WeatherType weatherType = weatherTypeRepository.findById(result.getWeatherId())
                            .orElseThrow(() -> new IllegalArgumentException("날씨 타입을 찾을 수 없습니다."));

                    RouteType routeType = routeTypeRepository.findById(result.getRouteId())
                            .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

                    Ending ending = endingRepository.findById(result.getEndingId())
                            .orElseThrow(() -> new IllegalArgumentException("엔딩을 찾을 수 없습니다."));

                    return new MyStatisticsResponse.RecentResult(
                            result.getResultId(),
                            weatherType.getWeatherName(),
                            routeType.getRouteName(),
                            ending.getEndingName(),
                            result.getCreatedAt()
                    );
                })
                .toList();
    }
}
// 내 통계+ 최근결과 5개