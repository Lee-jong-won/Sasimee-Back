package com.example.Sasimee_Back.controller.controllerAdvice;

import com.example.Sasimee_Back.Interceptor.JwtAuthenticateInterceptor;
import com.example.Sasimee_Back.ResponseCode.InterceptorExceptionCode;
import com.example.Sasimee_Back.common.BaseResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = JwtAuthenticateInterceptor.class)
public class JwtInterceptorExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<BaseResponse<Void>> expiredJwtExHandler(JwtException e){
        log.error("error={}", e);
        return BaseResponse.toResponseEntity(InterceptorExceptionCode.WRONG_TOKEN);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Void>> runtimeExHandler(RuntimeException e){
        log.error("error={}", e);
        return BaseResponse.toResponseEntity(InterceptorExceptionCode.UNKNOWN_TOKEN_SERVER_ERROR);
    }
}
