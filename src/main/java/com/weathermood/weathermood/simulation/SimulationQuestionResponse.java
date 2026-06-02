package com.weathermood.weathermood.simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SimulationQuestionResponse {

    private Long questionId;
    private String questionText;
    private Integer questionOrder;
    private List<SimulationChoiceResponse> choices;

    public static SimulationQuestionResponse from(SimulationQuestion question) {
        return new SimulationQuestionResponse(
                question.getQuestionId(),
                question.getQuestionText(),
                question.getQuestionOrder(),
                question.getSortedChoices()
                        .stream()
                        .map(SimulationChoiceResponse::from)
                        .toList()
        );
    }
}
//질문 entity > 프론트 응답 dto