package com.weathermood.weathermood.weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherTypeRepository extends JpaRepository<WeatherType, Long> {

    Optional<WeatherType> findByWeatherCode(String weatherCode);
}
//디비 접근. select 느낌.