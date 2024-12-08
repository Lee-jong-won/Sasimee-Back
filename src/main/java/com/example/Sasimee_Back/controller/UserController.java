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
        boolean isVerified = true;
        if(session.getAttribute("emailVerified") == null)
            isVerified = false;

        if(isVerified == false) return ResponseEntity.badRequest().
                body(UserDTO.registerResponse.builder()
                        .status(false)
                        .message("이메일 인증이 되지 않았습니다. 이메일 인증을 완료한 후 회원가입이 가능합니다.")
                        .build());

        UserDTO.registerResponse registerResponse = userService.register(registerRequest);
        session.removeAttribute("emailVerified");

        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO.loginResponse> loginController(@ModelAttribute UserDTO.loginRequest loginRequest,
                                                                 HttpSession session)
    {
        //중복 로그인 방지
        Object loginEmail = session.getAttribute("loginEmail");
        if(loginEmail != null) return ResponseEntity.badRequest().build();

        //아이디 또는 비밀번호가 틀린 경우 로그인 불가능
        User user = userService.login(loginRequest);
        if(user == null) return ResponseEntity.badRequest().build();

        session.setAttribute("loginEmail", user.getEmail());

        return ResponseEntity.ok(UserDTO.loginResponse.builder().
                email(user.getEmail()).
                name(user.getName()).
                address(user.getAddress()).
                phoneNumber(user.getPhoneNumber()).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutController(HttpSession session)
    {
        String loginEmail = (String)session.getAttribute("loginEmail");

        if(loginEmail == null) return ResponseEntity.badRequest().build();

        session.invalidate();
        return ResponseEntity.ok().build();
    }



}
