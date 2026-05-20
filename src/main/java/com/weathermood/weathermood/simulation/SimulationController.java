package com.weathermood.weathermood.simulation;

import com.weathermood.weathermood.global.ApiResponse;
import com.weathermood.weathermood.global.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @GetMapping("/questions")
    public ApiResponse<Map<String, List<SimulationQuestionResponse>>> getQuestions() {
        List<SimulationQuestionResponse> questions = simulationService.getQuestions();

        return ApiResponse.success(
                Map.of("questions", questions),
                "시뮬레이션 질문 조회에 성공했습니다."
        );
    }

    @PostMapping("/submit")
    public ApiResponse<SimulationSubmitResponse> submit(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody SimulationSubmitRequest request
    ) {
        SimulationSubmitResponse response =
                simulationService.submit(principal.getUserId(), request);

        return ApiResponse.success(response, "시뮬레이션 결과가 생성되었습니다.");
    }
}