package com.example.Sasimee_Back.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "클라이언트가 토큰 재발급을 위해 서버에게 보내는 토큰 정보")
public class TokenRequestDto {

    @Schema(example = "클라이언트가 발급받은 액세스 토큰")
    private String accessToken;
    @Schema(example = "클라이언트가 발급받은 액세스 토큰")
    private String refreshToken;

}
