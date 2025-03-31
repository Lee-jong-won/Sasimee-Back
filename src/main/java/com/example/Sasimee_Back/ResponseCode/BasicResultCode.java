package com.example.Sasimee_Back.ResponseCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BasicResultCode implements ResultCode {

    //200
    SUCCESS(HttpStatus.OK, "성공"),

    //4xx
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 api를 찾을 수 없습니다."), // 404
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."), // 400
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "서버로부터 인증된 요청이 아닙니다."), // 401
    FORBIDDEN(HttpStatus.FORBIDDEN, "서버로부터 인증된 요청이나, 해당 URL에 접근할 수 있는 요청은 아닙니다."), // 403
    UNKNOWN_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "알 수 없는 클라이언트 요청입니다."),

    //5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다"), // 500
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    BasicResultCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
