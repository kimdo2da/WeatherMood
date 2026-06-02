package com.weathermood.weathermood.users;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
}
//로그인요청
//{
//  "email": "jwt@test.com",
//  "password": "1234"
//}
//테스트중