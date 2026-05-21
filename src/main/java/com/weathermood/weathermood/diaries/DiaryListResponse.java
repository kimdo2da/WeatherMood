package com.weathermood.weathermood.diaries;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DiaryListResponse {

    private Long diaryId;
    private Long resultId;
    private String title;
    private String moodText;
    private String routeName;
    private String endingName;
    private LocalDateTime createdAt;
}