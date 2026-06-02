package com.weathermood.weathermood.simulation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "simulation_questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimulationQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "question_text", nullable = false, length = 255)
    private String questionText;

    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;

    @Column(name = "is_active", nullable = false)
    private Byte isActive;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<SimulationChoice> choices = new ArrayList<>();

    public List<SimulationChoice> getSortedChoices() {
        return choices.stream()
                .sorted(Comparator.comparing(SimulationChoice::getChoiceOrder))
                .toList();
    }
}
//db와 질문 1개에 선택지 여러개 프론트는 그래서 동일한 선택지가 순서가있음