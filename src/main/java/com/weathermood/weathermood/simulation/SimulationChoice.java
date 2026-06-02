package com.weathermood.weathermood.simulation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "simulation_choices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimulationChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Long choiceId;

    @Column(name = "choice_text", nullable = false, length = 255)
    private String choiceText;

    @Column(name = "choice_order", nullable = false)
    private Integer choiceOrder;

    @Column(name = "route_score", nullable = false)
    private Integer routeScore;

    @Column(name = "emotion_name", nullable = false, length = 50)
    private String emotionName;

    @Column(name = "emotion_score", nullable = false)
    private Integer emotionScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private SimulationQuestion question;

    @Column(name = "route_id", nullable = false)
    private Long routeId;
}
//특정 루트아이디 점수가 있음 선택한 점수를 합산해서 최종루트 대표감정을 계산
//여기서 route_id는 일단 Long routeId로 단순하게 받았어.
//나중에 RouteType 엔티티 만들면 @ManyToOne으로 바꿀 수도 있는데,
//지금 질문 조회 API에서는 루트 정보가 응답에 필요 없으니까 이게 더 간단해.
