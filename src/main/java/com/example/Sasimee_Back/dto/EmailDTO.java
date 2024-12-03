package com.example.Sasimee_Back.dto;

import lombok.Builder;
import lombok.Getter;

public class EmailDTO {

    @Getter
    @Builder
    public static class request {
        private String to;
        private String subject;
        private String message;
    }

}
