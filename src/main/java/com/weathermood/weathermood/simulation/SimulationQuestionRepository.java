package com.weathermood.weathermood.simulation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationQuestionRepository extends JpaRepository<SimulationQuestion, Long> {

    List<SimulationQuestion> findByIsActiveOrderByQuestionOrderAsc(Byte isActive);
}