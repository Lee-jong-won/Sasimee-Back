package com.example.Sasimee_Back.entity;


import lombok.Getter;

@Getter
public enum Gender {
    M("Man"), F("Female");

    private final String gender;

    Gender(String gender){
        this.gender = gender;
    }
}
