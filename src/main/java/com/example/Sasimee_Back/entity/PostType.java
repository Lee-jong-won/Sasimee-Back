package com.example.Sasimee_Back.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PostType {
    SURVEY("Survey"),
    TASK("Task");

    private final String postType;

    PostType(String postType) {
        this.postType = postType;
    }

    @JsonValue
    public String getPostType() {return postType;}

    @JsonCreator
    public static PostType fromString(String value) {
        for(PostType t : PostType.values()) {
            if(t.postType.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid post type value : " + value);
    }


}
