package com.example.Sasimee_Back.entity;

public enum Role {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String name;

    Role(String name){
        this.name = name;
    }

    public String getRoleName(){
        return name;
    }

}
