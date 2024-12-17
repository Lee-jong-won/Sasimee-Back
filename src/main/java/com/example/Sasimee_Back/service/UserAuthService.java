package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.config.Security.TokenProvider;
import com.example.Sasimee_Back.dto.TokenDto;
import com.example.Sasimee_Back.dto.TokenRequestDto;
import com.example.Sasimee_Back.dto.UserDTO;
import com.example.Sasimee_Back.entity.RefreshToken;
import com.example.Sasimee_Back.repository.RefreshTokenRepository;
import com.example.Sasimee_Back.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto login(UserDTO.loginRequest loginRequest) {

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //비밀번호가 다를 경우, 예외 발생!

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken();

        // 4. RefreshToken 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .key(authentication.getName())
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        // 5. 토큰 발급
        return TokenDto.builder().grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String accessToken){
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String userId = authentication.getName();
        refreshTokenRepository.deleteByKey(userId);
        SecurityContextHolder.clearContext();
    }


    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {

        // 1. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 2. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 3. Refresh Token 일치 하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 4. Refresh Token 검증 후 refresh token이 만료되지 않은 경우, access / refresh 재발급
        boolean isRefreshTokenValidated = tokenProvider.validateToken(tokenRequestDto.getRefreshToken());
        if (isRefreshTokenValidated) {
            String newRefreshToken = tokenProvider.generateRefreshToken();
            String newAccessToken = tokenProvider.createAccessToken(authentication);
            RefreshToken newRefreshTokenEntity = refreshToken.updateValue(newRefreshToken);
            refreshTokenRepository.save(newRefreshTokenEntity);

            return TokenDto.builder()
                    .grantType("bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        else
            throw new ExpiredJwtException(null, null, "refresh jwt token is expired!");

    }

}
