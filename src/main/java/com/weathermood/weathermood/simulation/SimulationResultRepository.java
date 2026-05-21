package com.weathermood.weathermood.simulation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface SimulationResultRepository extends JpaRepository<SimulationResult, Long> {

    Optional<SimulationResult> findByResultIdAndUserId(Long resultId, Long userId);

    List<SimulationResult> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<SimulationResult> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime createdAt);

    List<SimulationResult> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);
}

//findbyresultidanduserid 쓰는 이유
//resultId만으로 조회하면 남의 결과도 볼 수 있음
//resultId + userId로 조회하면 자기 결과만 볼 수 있음
//findBycreatedatafterorderbycreatedatdesc 24시간 전체통계
//findtop5byuseridorderbycreatedatdesc 개인 통계 최근 결과 5개용