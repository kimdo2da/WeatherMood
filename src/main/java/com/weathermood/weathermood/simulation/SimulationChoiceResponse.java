package com.weathermood.weathermood.simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimulationChoiceResponse {

    private Long choiceId;
    private String choiceText;
    private Integer choiceOrder;

    public static SimulationChoiceResponse from(SimulationChoice choice) {
        return new SimulationChoiceResponse(
                choice.getChoiceId(),
                choice.getChoiceText(),
                choice.getChoiceOrder()
        );
    }
}
//선택지 entity> 프론트 응답 dto