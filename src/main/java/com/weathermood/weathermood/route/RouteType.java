package com.weathermood.weathermood.route;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "route_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "route_name", nullable = false, length = 50)
    private String routeName;

    @Column(length = 255)
    private String description;
}