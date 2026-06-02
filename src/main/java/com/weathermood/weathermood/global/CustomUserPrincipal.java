package com.weathermood.weathermood.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserPrincipal {

    private Long userId;
    private String email;
}
//사용자 정보 담아두는 객체