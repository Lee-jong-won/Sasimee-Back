package com.example.Sasimee_Back.dto;

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
    @Schema(description = "태그 저장 및 조회를 위한 정보")
    public static class TagRequest{
        private String name;
        private TagCategory category;
    }
}
