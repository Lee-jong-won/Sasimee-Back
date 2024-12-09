package com.example.Sasimee_Back.entity;

import lombok.Getter;

@Getter
public enum PostType {
    S("설문형"), A("참여형");

    private final String postType;

    PostType(String postType) {this.postType = postType;}

    public static PostType fromString(String value) {
        if("설문형".equalsIgnoreCase(value)) {
            return S;
        } else if ("참여형".equalsIgnoreCase(value)) {
            return A;
        }else{
            throw new IllegalArgumentException("Invalid post type value: " + value);
        }
    }


}
