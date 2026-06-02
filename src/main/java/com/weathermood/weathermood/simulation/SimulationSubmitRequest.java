package com.weathermood.weathermood.simulation;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class SimulationSubmitRequest {

    private String weatherCode;
    private String weatherText;
    private BigDecimal temperature;
    private List<Long> selectedChoiceIds;
}
// 제출 요청 dto
//{
//  "weatherCode": "CLOUDS",
//  "weatherText": "broken clouds",
//  "temperature": 19.43,
//  "selectedChoiceIds": [1, 5, 9, 13]
//} 요청 json 형식 예시