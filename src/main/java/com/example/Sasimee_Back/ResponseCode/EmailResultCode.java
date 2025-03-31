package com.example.Sasimee_Back.ResponseCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EmailResultCode implements ResultCode{

    //200
    EMAIL_SEND_SUCCESS(HttpStatus.OK, "이메일 전송 성공"),
    VERIFY_SUCCESS(HttpStatus.OK, "인증 번호가 일치합니다"),

    //400
    VERIFY_FAILURE(HttpStatus.BAD_REQUEST, "잘못된 인증번호 입니다"),
    IS_ALREADY_MADE(HttpStatus.BAD_REQUEST, "이미 해당 이메일로 인증번호가 발급되었습니다"),

    //500
    EMAIL_SENDING_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 실패"),
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알려지지 않은 서버 에러입니다");

    private final HttpStatus httpStatus;
    private final String message;

    EmailResultCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
