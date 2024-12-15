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
    @Schema(description = "설문형 게시글 생성을 위한 요청 정보")
    public static class createSurveyRequest{
        @NotBlank(message = "제목은 필수 항목입니다.")
        @Schema(description = "게시글 생성을 위한 제목")
        private String title;

        @NotBlank(message = "내용은 필수 항목입니다.")
        @Schema(description = "설문에 대한 자세한 정보")
        private String content;

        @NotNull(message = "게시글 형태는 필수 항목입니다.")
        @Schema(description = "게시글의 속성 \"Survey\" 혹은 \"Task\" 을 입력해야 합니다.")
        private PostType postType;

        @NotNull(message = "설문형에서 구글폼 링크는 필수 항목입니다.")
        @Schema(description = "구글폼 설문 링크")
        private String survey;

        @Schema(description = "게시글 시작일 (yyyy-MM-dd")
        private String startDate;

        @Schema(description = "게시글 시작 시간 (HH:MM)")
        private String startTime;

        @Schema(description = "게시글 마감일 (yyyy-MM-dd)")
        private String endDate;

        @Schema(description = "게시글 마감 시간 (HH:MM)")
        private String endTime;

        @Schema(description = "실험자 이름 정보")
        private String author;

        @Schema(description = "게시글에 포함된 태그 요청 정보")
        private List<TagDTO.TagRequest> tags;
    }

    @Data
    @Builder
    @Schema(description = "수행형 게시글 생성을 위한 요청 정보")
    public static class createTaskRequest{
        @NotBlank(message = "제목은 필수 항목입니다.")
        @Schema(description = "게시글 생성을 위한 제목")
        private String title;

        @NotBlank(message = "내용은 필수 항목입니다.")
        @Schema(description = "실험에 대한 자세한 정보")
        private String content;

        @NotNull(message = "게시글 형태는 필수 항목입니다.")
        @Schema(description = "게시글의 속성 \"Survey\" 혹은 \"Task\" 을 입력해야 합니다.")
        private PostType postType;

        @Schema(description = "게시글 시작일 (yyyy-MM-dd")
        private String startDate;

        @Schema(description = "게시글 시작 시간 (HH:MM)")
        private String startTime;

        @Schema(description = "게시글 마감일 (yyyy-MM-dd)")
        private String endDate;

        @Schema(description = "게시글 마감 시간 (HH:MM)")
        private String endTime;

        @Schema(description = "실험자 이름 정보")
        private String author;

        @Schema(description = "지급되는 급여 정보")
        private Long payment;

        @Schema(description = "실험이 진행되는 주소 정보")
        private String address;

        @Schema(description = "게시글에 포함된 태그 요청 정보")
        private List<TagDTO.TagRequest> tags;
    }

    @Data
    @Builder
    @Schema(description = "게시글 생성 응답")
    public static class createSurveyResponse {
        private long id;
        private String title;
        private String content;
        private PostType postType;
        private String survey;
        private String startDate;
        private String startTime;
        private String endDate;
        private String endTime;
        private String author;
        private String timestamp;
        private List<TagDTO.TagRequest> tags;
    }

    @Data
    @Builder
    @Schema(description = "게시글 생성 응답")
    public static class createTaskResponse {
        private long id;
        private String title;
        private String content;
        private PostType postType;
        private String startDate;
        private String startTime;
        private String endDate;
        private String endTime;
        private String author;
        private Long payment;
        private String address;
        private String timestamp;
        private List<TagDTO.TagRequest> tags;
    }

    @Data
    @Schema(description = "게시글 삭제를 위한 요청 정보")
    public static class deleteRequest{
        @NotBlank(message = "삭제할 게시글의 id가 누락되었습니다.")
        @Schema(description = "게시글 삭제를 위한 게시글의 ID")
        private long id;
    }

    @Data
    @Builder
    @Schema(description = "특정 게시글 조회 응답")
    public static class getSurveyPostResponse{
        private long id;
        private String title;
        private String content;
        private PostType postType;
        private String survey;
        private String startDate;
        private String startTime;
        private String endDate;
        private String endTime;
        private List<TagDTO.TagRequest> tags;

        @Schema(description = "게시글 작성자")
        private String author;
    }

    @Data
    @Builder
    @Schema(description = "특정 게시글 조회 응답")
    public static class getTaskPostResponse{
        private long id;
        private String title;
        private String content;
        private PostType postType;
        private String startDate;
        private String startTime;
        private String endDate;
        private String endTime;
        private Long payment;
        private String address;
        private List<TagDTO.TagRequest> tags;

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
            private List<TagDTO.TagRequest> tags;
        }
    }
}
