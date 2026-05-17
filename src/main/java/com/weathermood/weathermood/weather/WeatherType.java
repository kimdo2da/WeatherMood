package com.weathermood.weathermood.weather;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weather_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeatherType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long weatherId;

    @Column(name = "weather_code", nullable = false, unique = true, length = 50)
    private String weatherCode;

    @Column(name = "weather_name", nullable = false, length = 50)
    private String weatherName;

    @Column(length = 255)
    private String description;
}