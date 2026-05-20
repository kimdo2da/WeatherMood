package com.weathermood.weathermood.results;

import com.weathermood.weathermood.global.ApiResponse;
import com.weathermood.weathermood.global.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/{resultId}")
    public ApiResponse<ResultDetailResponse> getResultDetail(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long resultId
    ) {
        ResultDetailResponse response =
                resultService.getResultDetail(principal.getUserId(), resultId);

        return ApiResponse.success(response, "OK");
    }
}