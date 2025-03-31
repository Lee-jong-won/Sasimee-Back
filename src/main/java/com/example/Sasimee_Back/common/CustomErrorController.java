package com.example.Sasimee_Back.common;

import com.example.Sasimee_Back.ResponseCode.BasicResultCode;
import com.example.Sasimee_Back.ResponseCode.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/error")
public class CustomErrorController extends AbstractErrorController {

    public CustomErrorController(ErrorAttributes errorAttributes){
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity<BaseResponse<Void>> customError(HttpServletRequest request){
        HttpStatus httpStatus = getStatus(request);
        String message = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)).
                get("message").toString();
        ResultCode errorCode;

        log.error("[BasicErrorHandler {} {} errMessage={}\n",
                request.getMethod(),
                request.getRequestURI(),
                message);

        if(httpStatus.is4xxClientError())
            errorCode = map4xxClientError(httpStatus);
        else
            errorCode = map5xxClientError(httpStatus);

         return BaseResponse.toResponseEntity(errorCode);
    }

    private BasicResultCode map4xxClientError(HttpStatus httpStatus){
        switch(httpStatus){
            case BAD_REQUEST:
                return BasicResultCode.BAD_REQUEST;
            case NOT_FOUND:
                return BasicResultCode.NOT_FOUND;
            case UNAUTHORIZED:
                return BasicResultCode.UNAUTHORIZED;
            case FORBIDDEN:
                return BasicResultCode.FORBIDDEN;
            default:
                return BasicResultCode.UNKNOWN_CLIENT_ERROR;
        }
    }

    private BasicResultCode map5xxClientError(HttpStatus httpStatus){
        switch(httpStatus){
            case INTERNAL_SERVER_ERROR:
                return BasicResultCode.INTERNAL_SERVER_ERROR;
            default:
                return BasicResultCode.UNKNOWN_SERVER_ERROR;
        }
    }

}
