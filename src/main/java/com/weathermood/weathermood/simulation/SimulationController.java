package com.weathermood.weathermood.simulation;

import com.weathermood.weathermood.global.ApiResponse;
import lombok.RequiredArgsConstructor;
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
}