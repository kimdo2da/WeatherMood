package com.weathermood.weathermood.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserPrincipal {

    private Long userId;
    private String email;
}