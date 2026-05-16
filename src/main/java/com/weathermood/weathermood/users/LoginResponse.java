package com.weathermood.weathermood.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private UserInfo user;

    public static LoginResponse of(String accessToken, User user) {
        return new LoginResponse(
                accessToken,
                "Bearer",
                new UserInfo(
                        user.getUserId(),
                        user.getEmail(),
                        user.getNickname()
                )
        );
    }

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String email;
        private String nickname;
    }
}