package com.weathermood.weathermood.users;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

    private String email;
    private String password;
    private String nickname;
}
//회원가입요청