package com.example.Sasimee_Back.dto;

import com.example.Sasimee_Back.entity.Tag;
import com.example.Sasimee_Back.entity.TagCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TagDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "태그 저장 및 조회를 위한 정보 -> 리스트로 json을 역 직렬화할 예정입니다.")
    public static class TagRequest{
        @Schema(description = "태그 이름", example = "여성")
        private String name;

        @Schema(description = "태그 이름", example = "GENDER")
        private TagCategory category;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "태그 응답을 위한 정보")
    public static class TagResponse{
        @Schema(name = "태그 이름")
        private String name;
        @Schema(name = "태그 종류")
        private TagCategory category;
    }



}
