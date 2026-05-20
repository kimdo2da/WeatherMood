package com.weathermood.weathermood.simulation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationChoiceRepository extends JpaRepository<SimulationChoice, Long> {

    List<SimulationChoice> findByChoiceIdIn(List<Long> choiceIds);
}