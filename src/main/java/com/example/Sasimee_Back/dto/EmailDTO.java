package com.example.Sasimee_Back.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class EmailDTO {

    @Getter
    @Builder
    public static class SentRequest {
        private String to;
        private String subject;
        private String message;
    }

    @Getter
    @Builder
    @Schema(description ="이메일 전송 응답")
    public static class SentResponse {
        @Schema(description = "이메일 전송 성공 여부", example = "true")
        private boolean status;
        @Schema(description = "이메일 전송에 따른 알림 메시지", example = "이메일이 성공적으로 전송되었습니다!")
        private String message;
        @Schema(description = "이메일 전송 대상", example = "example@example.com")
        private String receiver;
    }

    @Getter
    @Builder
    public static class VerifyMailRequest{
        private String email;
        private String authNum;
    }

    @Getter
    @Builder
    public static class VerifyMailResponse{
        private boolean status;
        private String message;
    }
}
