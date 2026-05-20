package com.weathermood.weathermood.ending;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ending_id")
    private Long endingId;

    @Column(name = "ending_name", nullable = false, length = 100)
    private String endingName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "condition_text", length = 255)
    private String conditionText;

    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "weather_id")
    private Long weatherId;
}