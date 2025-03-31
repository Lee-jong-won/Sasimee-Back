package com.example.Sasimee_Back.ResponseCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum InterceptorExceptionCode implements ResultCode {

    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다"),
    UNKNOWN_TOKEN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 검증 과정 중 오류가 발생했습니다");

    private final HttpStatus httpStatus;
    private final String message;

    InterceptorExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


}
