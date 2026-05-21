package com.weathermood.weathermood.diaries;

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

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final SimulationResultRepository simulationResultRepository;
    private final WeatherTypeRepository weatherTypeRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final EndingRepository endingRepository;

    @Transactional
    public DiaryResponse createDiary(Long userId, DiaryCreateRequest request) {
        validateCreateRequest(request);

        SimulationResult result = simulationResultRepository
                .findByResultIdAndUserId(request.getResultId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("내 시뮬레이션 결과를 찾을 수 없습니다."));

        Diary diary = new Diary(
                request.getTitle(),
                request.getContent(),
                request.getMoodText(),
                userId,
                result.getResultId()
        );

        Diary savedDiary = diaryRepository.save(diary);

        return DiaryResponse.from(savedDiary);
    }

    public List<DiaryListResponse> getMyDiaries(Long userId) {
        List<Diary> diaries = diaryRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return diaries.stream()
                .map(this::toDiaryListResponse)
                .toList();
    }

    public DiaryDetailResponse getDiaryDetail(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findByDiaryIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));

        SimulationResult result = simulationResultRepository
                .findByResultIdAndUserId(diary.getResultId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("연결된 결과를 찾을 수 없습니다."));

        DiaryDetailResponse.ResultSummary resultSummary = createResultSummary(result);

        return new DiaryDetailResponse(
                diary.getDiaryId(),
                diary.getResultId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getMoodText(),
                resultSummary,
                diary.getCreatedAt(),
                diary.getUpdatedAt()
        );
    }

    @Transactional
    public DiaryResponse updateDiary(Long userId, Long diaryId, DiaryUpdateRequest request) {
        validateUpdateRequest(request);

        Diary diary = diaryRepository.findByDiaryIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));

        diary.update(
                request.getTitle(),
                request.getContent(),
                request.getMoodText()
        );

        return DiaryResponse.from(diary);
    }

    @Transactional
    public Map<String, Long> deleteDiary(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findByDiaryIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));

        diaryRepository.delete(diary);

        return Map.of("diaryId", diaryId);
    }

    private DiaryListResponse toDiaryListResponse(Diary diary) {
        SimulationResult result = simulationResultRepository.findById(diary.getResultId())
                .orElseThrow(() -> new IllegalArgumentException("연결된 결과를 찾을 수 없습니다."));

        RouteType routeType = routeTypeRepository.findById(result.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

        Ending ending = endingRepository.findById(result.getEndingId())
                .orElseThrow(() -> new IllegalArgumentException("엔딩을 찾을 수 없습니다."));

        return new DiaryListResponse(
                diary.getDiaryId(),
                diary.getResultId(),
                diary.getTitle(),
                diary.getMoodText(),
                routeType.getRouteName(),
                ending.getEndingName(),
                diary.getCreatedAt()
        );
    }

    private DiaryDetailResponse.ResultSummary createResultSummary(SimulationResult result) {
        WeatherType weatherType = weatherTypeRepository.findById(result.getWeatherId())
                .orElseThrow(() -> new IllegalArgumentException("날씨 타입을 찾을 수 없습니다."));

        RouteType routeType = routeTypeRepository.findById(result.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("루트 타입을 찾을 수 없습니다."));

        Ending ending = endingRepository.findById(result.getEndingId())
                .orElseThrow(() -> new IllegalArgumentException("엔딩을 찾을 수 없습니다."));

        return new DiaryDetailResponse.ResultSummary(
                weatherType.getWeatherName(),
                routeType.getRouteName(),
                ending.getEndingName(),
                result.getMainEmotion()
        );
    }

    private void validateCreateRequest(DiaryCreateRequest request) {
        if (request.getResultId() == null) {
            throw new IllegalArgumentException("결과 ID는 필수입니다.");
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("일기 제목은 필수입니다.");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("일기 내용은 필수입니다.");
        }
    }

    private void validateUpdateRequest(DiaryUpdateRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("일기 제목은 필수입니다.");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("일기 내용은 필수입니다.");
        }
    }
}