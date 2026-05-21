package com.weathermood.weathermood.diaries;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DiaryDetailResponse {

    private Long diaryId;
    private Long resultId;
    private String title;
    private String content;
    private String moodText;
    private ResultSummary resultSummary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    public static class ResultSummary {
        private String weatherName;
        private String routeName;
        private String endingName;
        private String mainEmotion;
    }
}