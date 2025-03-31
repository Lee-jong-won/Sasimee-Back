package com.example.Sasimee_Back.Interceptor;

import com.example.Sasimee_Back.ResponseCode.InterceptorExceptionCode;
import com.example.Sasimee_Back.authentication.Admin;
import com.example.Sasimee_Back.common.BaseResponse;
import com.example.Sasimee_Back.service.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.admin atrribute가 true인 경우 -> 1-1.api가 admin 전용인 경우, 1-2.api가 admin도 user뿐만아니라 사용 가능한 경우
        //2.admin attribute가 false인 경우 -> 2-1.api가 admin 전용인 경우, 2-2.api가 user도  user뿐만 아니라 사용 가능한 경우
        //1-1 -> true, 1-2 -> false
        //2-1 -> false, 2-2 -> true

        Boolean isAdminJwt = Boolean.valueOf((String)request.getAttribute("isAdmin"));
        Boolean isAdminApi= checkAdminAnnotation(handler, Admin.class);
        Boolean notAuthorizedAccess =(isAdminApi == true) && (isAdminJwt == false);

        if(notAuthorizedAccess)
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(403);
            BaseResponse<Void> baseResponse = new BaseResponse<Void>(InterceptorExceptionCode.WRONG_TOKEN.getHttpStatus(), InterceptorExceptionCode.WRONG_TOKEN.getMessage(), null);
            String result = objectMapper.writeValueAsString(baseResponse);
            response.getWriter().write(result);
            return false;
        }
        else
            return true;
    }

    Boolean checkAdminAnnotation(Object handler, Class cls){
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        if(handlerMethod.getMethodAnnotation(cls) != null)
            return true;
        else
            return false;
    }

}
