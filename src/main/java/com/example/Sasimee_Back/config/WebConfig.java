package com.example.Sasimee_Back.config;

import com.example.Sasimee_Back.argumentResolver.JwtAuthenticationArgumentResolver;
import com.example.Sasimee_Back.Interceptor.JwtAuthenticateInterceptor;
import com.example.Sasimee_Back.Interceptor.JwtAuthorizationInterceptor;
import com.example.Sasimee_Back.service.TokenProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    //tokenProvider bean을 주입 받아야 하는 것들
    private final JwtAuthenticateInterceptor jwtAuthenticateInterceptor;
    private final JwtAuthorizationInterceptor jwtAuthorizationInterceptor;
    private final JwtAuthenticationArgumentResolver jwtAuthenticationArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthenticationArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(jwtAuthenticateInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/email/*", "/favicon.ico",
                                                "/user/reissue", "/swagger/*", "/swagger-ui/*", "/api-docs/*", "/api-docs", "/error");
        interceptorRegistry.addInterceptor(jwtAuthorizationInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/email/*", "/favicon.ico",
                                                "/user/reissue", "/swagger/*", "/swagger-ui/*", "/api-docs/*", "/api-docs", "/error");
    }

}
