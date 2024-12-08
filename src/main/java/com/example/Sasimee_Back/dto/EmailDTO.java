package com.example.Sasimee_Back.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class EmailDTO {

    @Getter
    @Builder
    @Schema(description = "이메일 전송 요청")
    public static class SentRequest {
        @Schema(example = "이메일 전송 대상")
        private String to;
        @Schema(example = "이메일 제목")
        private String subject;
        @Schema(description = "인증 번호 api 요청시, null값 전달", example = "이메일 내용")
        private String message;
    }

    @Getter
    @Builder
    @Schema(description ="이메일 전송 응답")
    public static class SentResponse {
        @Schema(example = "이메일 전송 성공 여부")
        private boolean status;
        @Schema(example = "이메일 전송 성공 여부에 따른 메시지")
        private String message;
        @Schema(example = "이메일 전송 대상 이메일")
        private String receiver;
    }

    @Getter
    @Builder
    @Schema
    public static class VerifyMailRequest{
        @Schema(example = "인증 번호를 받은 이메일")
        private String email;
        @Schema(example = "이메일로 전송된 인증번호")
        private String authNum;
    }

    @Getter
    @Builder
    @Schema
    public static class VerifyMailResponse{
        @Schema(example = "이메일 인증 성공 여부")
        private boolean status;
        @Schema(example = "이메일 인증 성공 여부에 따른 메시지")
        private String message;
    }
}
