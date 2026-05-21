package com.weathermood.weathermood.statistics;

import com.weathermood.weathermood.global.ApiResponse;
import com.weathermood.weathermood.global.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/recent")
    public ApiResponse<RecentStatisticsResponse> getRecentStatistics() {
        RecentStatisticsResponse response =
                statisticsService.getRecentStatistics();

        return ApiResponse.success(response, "최근 통계 조회에 성공했습니다.");
    }

    @GetMapping("/me")
    public ApiResponse<MyStatisticsResponse> getMyStatistics(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        MyStatisticsResponse response =
                statisticsService.getMyStatistics(principal.getUserId());

        return ApiResponse.success(response, "개인 통계 조회에 성공했습니다.");
    }
}