package com.example.Sasimee_Back.ResponseCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.xml.transform.Result;


@Getter
public enum UserResultCode implements ResultCode {

    //200
    SUCCESS_REGISTER(HttpStatus.OK, "회원가입 성공"),
    SUCCESS_REISSUE_TOKEN(HttpStatus.OK, "토큰 재발급 성공"),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인 성공"),
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃 성공"),

    //400
    NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증 후, 다시 시도해주세요"),
    WRONG_INFO(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다"),
    WRONG_JWT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰 정보입니다"),

    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다");

    private final HttpStatus httpStatus;
    private final String message;

    UserResultCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


}
