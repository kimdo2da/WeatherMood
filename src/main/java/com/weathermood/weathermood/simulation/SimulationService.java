package com.weathermood.weathermood.simulation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimulationService {

    private final SimulationQuestionRepository simulationQuestionRepository;

    public List<SimulationQuestionResponse> getQuestions() {
        List<SimulationQuestion> questions =
                simulationQuestionRepository.findByIsActiveOrderByQuestionOrderAsc((byte)1);

        return questions.stream()
                .map(SimulationQuestionResponse::from)
                .toList();
    }
}