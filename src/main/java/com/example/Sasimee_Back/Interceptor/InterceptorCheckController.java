package com.example.Sasimee_Back.Interceptor;

import com.example.Sasimee_Back.authentication.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interceptor/check")
public class InterceptorCheckController {

    @RequestMapping("/authentication")
    public String checkAuthentication(){
        return "authentication success";
    }

    @Admin
    @RequestMapping("/authorization")
    public String checkAuthorization(){
        return "authorization success";
    }

}
