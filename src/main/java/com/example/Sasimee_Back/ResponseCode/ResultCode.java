package com.example.Sasimee_Back.ResponseCode;

import org.springframework.http.HttpStatus;


public interface ResultCode {
    public HttpStatus getHttpStatus();
    public String getMessage();
}
