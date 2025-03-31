package com.example.Sasimee_Back.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailDTO {

    @Getter
    @Schema(description = "이메일 전송 요청")
    public static class SentRequest {
        @Schema(example = "이메일 전송 대상")
        @NotBlank
        @Email(message = "잘못된 이메일 형식입니다.")
        private String to;
        @Schema(example = "이메일 제목")
        private String subject;
    }

    @Getter
    @Schema
    public static class VerifyMailRequest{
        @Schema(example = "인증 번호를 받은 이메일")
        private String email;
        @Schema(example = "이메일로 전송된 인증번호")
        private String authNum;
    }

}
