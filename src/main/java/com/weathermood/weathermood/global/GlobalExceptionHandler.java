package com.weathermood.weathermood.global;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiErrorResponse.fail("INVALID_REQUEST", e.getMessage());
    }
    //service 예외처리,오류 응답 형식

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleException(Exception e) {
        return ApiErrorResponse.fail(
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다."
        );
    }
} //일반 예상치못한 오류 500응답.
//컨트롤러에서 발생하는 예외를 찾아 json응답으로 바꿔주는 역할.