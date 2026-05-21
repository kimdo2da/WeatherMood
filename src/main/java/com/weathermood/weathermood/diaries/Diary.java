package com.weathermood.weathermood.diaries;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "diaries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "mood_text", length = 50)
    private String moodText;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "result_id", nullable = false)
    private Long resultId;

    public Diary(
            String title,
            String content,
            String moodText,
            Long userId,
            Long resultId
    ) {
        this.title = title;
        this.content = content;
        this.moodText = moodText;
        this.userId = userId;
        this.resultId = resultId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content, String moodText) {
        this.title = title;
        this.content = content;
        this.moodText = moodText;
        this.updatedAt = LocalDateTime.now();
    }
}