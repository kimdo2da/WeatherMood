package com.weathermood.weathermood.contents;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "content_type", nullable = false, length = 30)
    private String contentType;

    @Column(length = 100)
    private String genre;

    @Column(name = "mood_tag", length = 255)
    private String moodTag;

    @Column(name = "weather_tag", length = 255)
    private String weatherTag;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @Column(name = "external_api_id", length = 100)
    private String externalApiId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "route_id", nullable = false)
    private Long routeId;
}
//db기반이지만 사용은 안함 혹시 몰라 남겨둠
// route_id 기준으로 콘텐츠 찾기 일단