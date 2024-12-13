package com.example.Sasimee_Back.config.Security;

import com.example.Sasimee_Back.config.Security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    //Authorization 필터에서 발생하는 예외 -> accessdDeniedHandler가 처리
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (REST API 사용 시 주로 비활성화)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/register", "/email/**", "/user/reissue", "/swagger-ui/**", "/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()// 그 외 모든 요청은 인증 필요
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                 .exceptionHandling(exception -> exception
                         .authenticationEntryPoint((request, response, authException) -> {
                             String errorMessage;

                             if (authException instanceof BadCredentialsException) {
                                 errorMessage = "Email or password is wrong!";
                                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                             }
                             else {
                                 errorMessage = "Authentication failed";
                                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                             }

                             response.setContentType("application/json");
                             response.setCharacterEncoding("UTF-8");
                             response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
                         })
                 );

        return http.build();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
