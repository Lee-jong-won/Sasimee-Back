package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.EmailDTO;
import com.example.Sasimee_Back.service.EmailAuthService;
import com.example.Sasimee_Back.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/email")
@RestController
public class EmailController {

    private EmailService emailService;
    private EmailAuthService emailAuthService;

    @PostMapping("/send")
    public ResponseEntity<EmailDTO.SentResponse> sendMail(@RequestBody EmailDTO.SentRequest emailRequest) {

        EmailDTO.SentResponse response = emailService.sendEmailNotice(emailRequest, "email");
        emailAuthService.saveVerificationCode(emailRequest.getTo(), EmailAuthService.createdCertifyNum());

        if(response.isStatus() == false) {
            emailAuthService.deleteVerificationCode(emailRequest.getTo());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<EmailDTO.VerifyMailResponse> verifyMail(@RequestBody EmailDTO.VerifyMailRequest request,
    HttpSession session) {

        boolean verified = emailAuthService.verifyEmail(request.getEmail(), request.getAuthNum());

        if (!verified)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(EmailDTO.VerifyMailResponse.builder().status(false)
                            .message("인증 번호가 틀립니다!")
                            .build());
        else {
            session.setAttribute("emailVerified", true);
            session.setAttribute("verifiedEmail", request.getEmail());

            return ResponseEntity.ok(EmailDTO.VerifyMailResponse.builder().status(true)
                    .message("이메일 인증이 성공적으로 완료되었습니다.")
                    .build());
        }


    }
}
