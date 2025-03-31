package com.example.Sasimee_Back.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

public class RefreshTokenExpireException extends JwtException {
    public RefreshTokenExpireException(String message){
        super(message);
    }

}
