package com.weathermood.weathermood.simulation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimulationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "main_emotion", nullable = false, length = 50)
    private String mainEmotion;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @Column(name = "weather_text", length = 100)
    private String weatherText;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "recommended_contents", columnDefinition = "JSON")
    private String recommendedContents;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "weather_id", nullable = false)
    private Long weatherId;

    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "ending_id", nullable = false)
    private Long endingId;

    public SimulationResult(
            String mainEmotion,
            Integer totalScore,
            String weatherText,
            BigDecimal temperature,
            String recommendedContents,
            Long userId,
            Long weatherId,
            Long routeId,
            Long endingId
    ) {
        this.mainEmotion = mainEmotion;
        this.totalScore = totalScore;
        this.weatherText = weatherText;
        this.temperature = temperature;
        this.recommendedContents = recommendedContents;
        this.userId = userId;
        this.weatherId = weatherId;
        this.routeId = routeId;
        this.endingId = endingId;
        this.createdAt = LocalDateTime.now();
    }
}
// 결과저장