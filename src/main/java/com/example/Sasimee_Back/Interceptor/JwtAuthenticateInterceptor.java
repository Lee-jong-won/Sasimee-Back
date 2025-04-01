package com.example.Sasimee_Back.Interceptor;

import com.example.Sasimee_Back.ResponseCode.InterceptorExceptionCode;
import com.example.Sasimee_Back.common.BaseResponse;
import com.example.Sasimee_Back.service.TokenProvider;
import com.example.Sasimee_Back.exception.AuthInterceptorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticateInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("path={}", request.getServletPath());

        String jwt = tokenProvider.resolveToken(request);

        if(jwt == null){
            log.error("authorization 헤더가 없거나, 올바르지 않은 형식입니다");
            throw new AuthInterceptorException("authorization 헤더가 없거나, 올바르지 않은 형식입니다");
        }

        if(tokenProvider.validateToken(jwt)) {
            String isAdmin = (String)tokenProvider.parseClaims(jwt).get("isAdmin");
            request.setAttribute("isAdmin", isAdmin);
            return true;
        }
        else {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            BaseResponse<Void> baseResponse = new BaseResponse<Void>(InterceptorExceptionCode.WRONG_TOKEN.getHttpStatus(), InterceptorExceptionCode.WRONG_TOKEN.getMessage(), null);
            String result = objectMapper.writeValueAsString(baseResponse);
            response.getWriter().write(result);
            return false;
        }
    }
}
