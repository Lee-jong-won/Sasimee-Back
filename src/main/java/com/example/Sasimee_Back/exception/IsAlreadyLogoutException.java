package com.example.Sasimee_Back.exception;

public class IsAlreadyLogoutException extends RuntimeException {
    public IsAlreadyLogoutException(String message){
        super(message);
    }
}
