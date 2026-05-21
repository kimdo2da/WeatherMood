package com.weathermood.weathermood.diaries;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DiaryResponse {

    private Long diaryId;
    private Long resultId;
    private String title;
    private String content;
    private String moodText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(
                diary.getDiaryId(),
                diary.getResultId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getMoodText(),
                diary.getCreatedAt(),
                diary.getUpdatedAt()
        );
    }
}