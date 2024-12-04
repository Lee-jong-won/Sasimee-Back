package com.example.Sasimee_Back.dto;

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
    public static class SentResponse {
        private boolean status;
        private String message;
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
