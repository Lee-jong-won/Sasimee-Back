package com.example.Sasimee_Back.exception;

import io.jsonwebtoken.JwtException;

public class AuthInterceptorException extends JwtException {
    public AuthInterceptorException(String message){
        super(message);
    }

}
