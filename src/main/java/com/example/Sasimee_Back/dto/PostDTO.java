package com.example.Sasimee_Back.dto;

import com.example.Sasimee_Back.entity.Post;
import com.example.Sasimee_Back.entity.PostType;
import com.example.Sasimee_Back.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class PostDTO {
    @Data
    @Builder
    public static class createRequest{
        @NotBlank(message = "제목은 필수 항목입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 항목입니다.")
        private String content;

        @NotNull(message = "게시글 형태는 필수 항목입니다.")
        private PostType postType;

        private String survey;

        private String deadline;

        private List<String> tags;

        @NotNull(message = "유저의 정보는 필수 항목입니다.")
        private Long userId;
    }

    @Data
    @Builder
    public static class createResponse {
        private long id;
        private String title;
        private String content;
        private PostType postType;
        private String survey;
        private String deadline;
        private String timestamp;
        private List<String> tags;
    }

    @Data
    public static class deleteRequest{
        @NotBlank(message = "삭제할 게시글의 id가 누락되었습니다.")
        private long id;
    }

    @Data
    public static class getPostResponse{
        private long id;
        private String title;
        private String content;
        private PostType postType;
        private String survey;
        private String deadline;
        private List<String> tags;
        private String author;
    }

    @Data
    @Builder
    public static class getAllPostResponse{
        private List<PostSummary> posts;
        private int currentPage;
        private int totalPage;
        private long totalElements;

        @Data
        @Builder
        public static class PostSummary{
            private long id;
            private String title;
            private PostType postType;
            private List<String> tags;
        }
    }
}
