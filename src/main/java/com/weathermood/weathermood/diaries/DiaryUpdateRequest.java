package com.weathermood.weathermood.diaries;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryUpdateRequest {

    private String title;
    private String content;
    private String moodText;
}