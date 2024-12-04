package com.example.Sasimee_Back.controller;


import com.example.Sasimee_Back.dto.UserDTO;
import com.example.Sasimee_Back.entity.User;
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
            HttpSession session)
    {
        String loginUser = (String)session.getAttribute("loginUser");
        Boolean isVerified = (boolean)session.getAttribute("emailVerified");

        if(loginUser != null) return ResponseEntity.badRequest().
                body(UserDTO.registerResponse.builder()
                        .status(false)
                        .message("이미 로그인한 상태에서 회원가입이 불가능합니다.")
                        .build());

        if(isVerified == false) return ResponseEntity.badRequest().
                body(UserDTO.registerResponse.builder()
                        .status(false)
                        .message("이메일 인증이 되지 않았습니다. 이메일 인증을 완료한 후 회원가입이 가능합니다.")
                        .build());

        UserDTO.registerResponse registerResponse = userService.register(registerRequest);

        session.setAttribute("loginUser", registerRequest.getEmail());
        session.removeAttribute("emailVerified");
        
        return ResponseEntity.ok(registerResponse);
    }



}
