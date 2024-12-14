package com.example.Sasimee_Back.dto;

import com.example.Sasimee_Back.entity.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class PostDTO {
    @Data
    @Builder
    @Schema(description = "포스트 작성을 위한 정보")
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
    @Schema(description = "포스트 작성 응답")
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
    @Schema(description = "포스트 삭제를 위한 정보")
    public static class deleteRequest{
        @NotBlank(message = "삭제할 포스트의 id가 누락되었습니다.")
        private long id;
    }

    @Data
    @Schema(description = "특정 게시글에 대한 조회 응답")
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
    @Schema(description = "게시글 다수에 대한 조회 응답 ")
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
