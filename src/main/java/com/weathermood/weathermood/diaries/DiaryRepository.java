package com.weathermood.weathermood.diaries;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Diary> findByDiaryIdAndUserId(Long diaryId, Long userId);
}