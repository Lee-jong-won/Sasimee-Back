package com.example.Sasimee_Back.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TagCategory {
    GENDER,     //성별 태그
    AGE_GROUP,  //연령대 태그
    TOPIC,      //주제 태그
    CLASS;      //실험 유형 태그

    @JsonCreator
    private static TagCategory fromString(@JsonProperty("category")String category){
        for( TagCategory tagCategory : TagCategory.values()){
            if(tagCategory.toString().equalsIgnoreCase(category))
                return tagCategory;
        }
        throw new IllegalArgumentException("Invalid value for TagCategory:" + category);
    }

}
