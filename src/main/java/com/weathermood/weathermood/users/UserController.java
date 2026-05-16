package com.weathermood.weathermood.users;

import com.weathermood.weathermood.global.ApiResponse;
import com.weathermood.weathermood.global.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@RequestBody SignupRequest request) {
        UserResponse response = userService.signup(request);

        return ApiResponse.success(response, "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        return ApiResponse.success(response, "로그인에 성공했습니다.");
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyInfo(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        UserResponse response = userService.getMyInfo(principal.getUserId());

        return ApiResponse.success(response, "OK");
    }
}