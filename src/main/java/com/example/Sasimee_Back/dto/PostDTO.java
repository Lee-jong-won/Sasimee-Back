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
    @Schema(description = "게시글 생성을 위한 요청 정보")
    public static class createRequest{
        @NotBlank(message = "제목은 필수 항목입니다.")
        @Schema(description = "게시글 생성을 위한 제목")
        private String title;

        @NotBlank(message = "내용은 필수 항목입니다.")
        @Schema(description = "게시글 생성을 위한 내용")
        private String content;

        @NotNull(message = "게시글 형태는 필수 항목입니다.")
        @Schema(description = "게시글의 속성 - S는 설문형, A는 참여형")
        private PostType postType;

        @Schema(description = "구글폼 설문 링크")
        private String survey;

        @Schema(description = "게시글 마감일")
        private String deadline;

        @Schema(description = "게시글에 포함된 태그들의 이름")
        private List<String> tags;
    }

    @Data
    @Builder
    @Schema(description = "게시글 생성 응답")
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
    @Schema(description = "게시글 삭제를 위한 요청 정보")
    public static class deleteRequest{
        @NotBlank(message = "삭제할 게시글의 id가 누락되었습니다.")
        @Schema(description = "게시글 삭제를 위한 게시글의 ID")
        private long id;
    }

    @Data
    @Schema(description = "특정 게시글 조회 응답")
    public static class getPostResponse{
        private long id;
        private String title;
        private String content;
        private PostType postType;
        private String survey;
        private String deadline;
        private List<String> tags;

        @Schema(description = "게시글 작성자")
        private String author;
    }

    @Data
    @Builder
    @Schema(description = "다수의 게시글들 조회 응답")
    public static class getAllPostResponse{
        private List<PostSummary> posts;
        @Schema(description = "게시글들이 포함된 페이지의 index")
        private int currentPage;

        @Schema(description = "모든 페이지의 수")
        private int totalPage;
        private long totalElements;

        @Data
        @Builder
        @Schema(description = "요약된 게시글 정보")
        public static class PostSummary{
            private long id;
            private String title;
            private PostType postType;
            private List<String> tags;
        }
    }
}
