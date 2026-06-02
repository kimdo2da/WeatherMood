package com.weathermood.weathermood.users;

import com.weathermood.weathermood.global.ApiResponse;
import com.weathermood.weathermood.global.CustomUserPrincipal;
import com.weathermood.weathermood.results.MyResultResponse;
import com.weathermood.weathermood.results.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users") //간편화 
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResultService resultService;

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@RequestBody SignupRequest request) {
        UserResponse response = userService.signup(request);

        return ApiResponse.success(response, "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        return ApiResponse.success(response, "로그인에 성공했습니다.");
    } //jwt 토큰 줌

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyInfo(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        UserResponse response = userService.getMyInfo(principal.getUserId());

        return ApiResponse.success(response, "OK");
    } //토큰필요

    @GetMapping("/me/results")
    public ApiResponse<Map<String, List<MyResultResponse>>> getMyResults(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<MyResultResponse> results =
                resultService.getMyResults(principal.getUserId());

        return ApiResponse.success(
                Map.of("items", results),
                "OK"
        );
    }
} //토큰필요 얘는 resultservice도 사용함 items 배열로 감싸서 반환함.