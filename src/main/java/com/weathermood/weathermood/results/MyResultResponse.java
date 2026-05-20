package com.weathermood.weathermood.results;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyResultResponse {

    private Long resultId;
    private String weatherName;
    private String routeName;
    private String endingName;
    private String mainEmotion;
    private Integer totalScore;
    private LocalDateTime createdAt;
}