package com.example.Sasimee_Back.controller;


import com.example.Sasimee_Back.dto.UserDTO;
import com.example.Sasimee_Back.service.EmailAuthService;
import com.example.Sasimee_Back.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO.registerResponse> registerController(
            @RequestBody UserDTO.registerRequest registerRequest,
            HttpServletRequest servletRequest,
            @SessionAttribute(value = "loginUser", required = false) String loginUser)
    {
        if(loginUser != null) return ResponseEntity.badRequest().build();

        UserDTO.registerResponse registerResponse = userService.register(registerRequest);

        HttpSession session = servletRequest.getSession();
        session.setAttribute("loginUser", registerResponse.getEmail());

        return ResponseEntity.ok(registerResponse);
    }



}
