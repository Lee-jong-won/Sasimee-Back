package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.TokenDto;
import com.example.Sasimee_Back.dto.TokenRequestDto;
import com.example.Sasimee_Back.entity.RefreshToken;
import com.example.Sasimee_Back.exception.IsAlreadyLogoutException;
import com.example.Sasimee_Back.exception.RefreshTokenExpireException;
import com.example.Sasimee_Back.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class TokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    public final Key key;
    public final RefreshTokenRepository refreshTokenRepository;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createAccessToken(String memberEmail, Boolean isAdmin, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("memberEmail", memberEmail);
        claims.put("isAdmin", Boolean.toString(isAdmin));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + expirationTime);
        String accessToken = Jwts.builder()
                .setSubject(memberEmail)       // payload "sub": "name"
                .addClaims(claims)        // payload "auth": "USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 151621022 (ex)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        return accessToken;
    }

    public String createRefreshToken(String memberEmail, long expireTime) {
        long now = (new Date()).getTime();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + expireTime))
                .setSubject(memberEmail)
                .claim("memberEmail", memberEmail)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        refreshTokenRepository.save(new RefreshToken(memberEmail, refreshToken));
        return refreshToken;
    }

    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        String memberEmail = null;
        try {
            memberEmail = (String) parseClaims(tokenRequestDto.getRefreshToken()).get("memberEmail");
        }catch(ExpiredJwtException e){
            throw new RefreshTokenExpireException("전달된 refresh token은 만료되었습니다");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByKey(memberEmail)
                .orElseThrow(() -> new IsAlreadyLogoutException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new JwtException("user info in token is not correct!");
        }

        String newAccessToken = createAccessToken(memberEmail, false, ACCESS_TOKEN_EXPIRE_TIME);
        TokenDto tokenDto = TokenDto.builder()
                .grantType("bearer")
                .accessToken(newAccessToken)
                .build();

        return tokenDto;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw e;
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }

}
