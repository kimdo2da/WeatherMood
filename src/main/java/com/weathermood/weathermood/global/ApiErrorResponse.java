package com.weathermood.weathermood.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    private boolean success;
    private ErrorBody error;

    public static ApiErrorResponse fail(String code, String message) {
        return new ApiErrorResponse(false, new ErrorBody(code, message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorBody {
        private String code;
        private String message;
    }
}
//실패 응답 공통형식