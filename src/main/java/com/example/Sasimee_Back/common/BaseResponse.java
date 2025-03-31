package com.example.Sasimee_Back.common;

import com.example.Sasimee_Back.ResponseCode.ResultCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
public class BaseResponse<T> {

    private HttpStatus httpStatus = HttpStatus.OK;
    private String message = null;
    private T data;

    public BaseResponse(T data) {this.data = data;}

    public BaseResponse(HttpStatus httpStatus, String message, T data){
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> from(ResultCode resultCode, T data){
        return new BaseResponse<T>(resultCode.getHttpStatus(), resultCode.getMessage(), data);
    }

    public static BaseResponse<Void> from(ResultCode resultCode){
        return new BaseResponse<>(resultCode.getHttpStatus(), resultCode.getMessage(), null);
    }

    public static <T> ResponseEntity<BaseResponse<T>> toResponseEntity(ResultCode resultCode, T data){
        return ResponseEntity
                .status(resultCode.getHttpStatus())
                .body(new BaseResponse(resultCode.getHttpStatus(), resultCode.getMessage(), data));
    }
    
    public static ResponseEntity<BaseResponse<Void>> toResponseEntity(ResultCode resultCode){
        return ResponseEntity
                .status(resultCode.getHttpStatus())
                .body(new BaseResponse<Void>(resultCode.getHttpStatus(), resultCode.getMessage(), null));
    }


}
