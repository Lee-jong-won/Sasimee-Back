package com.example.Sasimee_Back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class GeminiDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeminiRequest{
        private List<Content> contents;

        @Data
        public static class Content {
            private List<Part> parts;

            public Content(String text){
                parts = new ArrayList<>();
                Part part = new Part(text);
                parts.add(part);
            }

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Part{
                private String text;
            }
        }

        public void createGeminiRequest(String text){
            this.contents = new ArrayList<>();
            Content content = new Content(text);
            contents.add(content);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeminiResponse{
        private List<Candidate> candidates;

        @Data
        public static class Candidate{
            private Content content;
            private String finishReason;
        }

        @Data
        public static class Content{
            private List<Part> parts;
            private String role;
        }

        @Data
        public static class Part{
            private String text;
        }
    }
}
