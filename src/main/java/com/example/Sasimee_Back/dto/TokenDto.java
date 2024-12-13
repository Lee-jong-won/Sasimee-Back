package com.example.Sasimee_Back.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "클라이언트에게 발급되는 token들의 정보")
public class TokenDto {
    @Schema(example="승인 권한")
    private String grantType;
    @Schema(example="액세스 토큰")
    private String accessToken;
    @Schema(example="리프레시 토큰")
    private String refreshToken;
}
