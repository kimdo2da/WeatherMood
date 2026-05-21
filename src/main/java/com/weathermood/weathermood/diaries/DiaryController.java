package com.weathermood.weathermood.diaries;

import com.weathermood.weathermood.global.ApiResponse;
import com.weathermood.weathermood.global.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ApiResponse<DiaryResponse> createDiary(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody DiaryCreateRequest request
    ) {
        DiaryResponse response =
                diaryService.createDiary(principal.getUserId(), request);

        return ApiResponse.success(response, "일기가 작성되었습니다.");
    }

    @GetMapping
    public ApiResponse<Map<String, List<DiaryListResponse>>> getMyDiaries(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<DiaryListResponse> diaries =
                diaryService.getMyDiaries(principal.getUserId());

        return ApiResponse.success(
                Map.of("items", diaries),
                "OK"
        );
    }

    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryDetailResponse> getDiaryDetail(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long diaryId
    ) {
        DiaryDetailResponse response =
                diaryService.getDiaryDetail(principal.getUserId(), diaryId);

        return ApiResponse.success(response, "OK");
    }

    @PutMapping("/{diaryId}")
    public ApiResponse<DiaryResponse> updateDiary(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long diaryId,
            @RequestBody DiaryUpdateRequest request
    ) {
        DiaryResponse response =
                diaryService.updateDiary(principal.getUserId(), diaryId, request);

        return ApiResponse.success(response, "일기가 수정되었습니다.");
    }

    @DeleteMapping("/{diaryId}")
    public ApiResponse<Map<String, Long>> deleteDiary(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long diaryId
    ) {
        Map<String, Long> response =
                diaryService.deleteDiary(principal.getUserId(), diaryId);

        return ApiResponse.success(response, "일기가 삭제되었습니다.");
    }
}