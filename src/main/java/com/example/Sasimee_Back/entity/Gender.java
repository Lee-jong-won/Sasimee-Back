package com.example.Sasimee_Back.entity;


import lombok.Getter;

@Getter
public enum Gender {
    M("Man"), F("Female");

    private final String gender;

    Gender(String gender){
        this.gender = gender;
    }


    public static Gender fromString(String value) {
        if ("male".equalsIgnoreCase(value)) {
            return M;
        } else if ("female".equalsIgnoreCase(value)) {
            return F;
        } else {
            throw new IllegalArgumentException("Invalid gender value: " + value);
        }
    }

}
