package com.weathermood.weathermood.ending;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndingRepository extends JpaRepository<Ending, Long> {

    Optional<Ending> findFirstByRouteIdAndWeatherId(Long routeId, Long weatherId);

    Optional<Ending> findFirstByRouteIdAndWeatherIdIsNull(Long routeId);
}