package com.example.Sasimee_Back.argumentResolver;

import com.example.Sasimee_Back.service.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasJwtAuthenticationAnnotation = parameter.hasParameterAnnotation(JwtAuthentication.class);
        boolean hasMemberEmailType = String.class.isAssignableFrom(parameter.getParameterType());
        return hasJwtAuthenticationAnnotation && hasMemberEmailType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = tokenProvider.resolveToken(request);
        String memberEmail = tokenProvider.parseClaims(accessToken).get("memberEmail", String.class);
        return memberEmail;
    }
}
