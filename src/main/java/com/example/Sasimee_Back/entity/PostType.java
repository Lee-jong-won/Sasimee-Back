package com.example.Sasimee_Back.entity;

import lombok.Getter;

@Getter
public enum PostType {
    S("Survey"), T("Task");

    private final String postType;

    PostType(String postType) {this.postType = postType;}

    public static PostType fromString(String value) {
        if("Survey".equalsIgnoreCase(value)) {
            return S;
        } else if ("Task".equalsIgnoreCase(value)) {
            return T;
        }else{
            throw new IllegalArgumentException("Invalid post type value: " + value);
        }
    }


}
