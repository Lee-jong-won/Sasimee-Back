package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.EmailDTO;
import com.example.Sasimee_Back.service.EmailAuthService;
import com.example.Sasimee_Back.service.EmailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/email")
@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/send")
    public ResponseEntity<EmailDTO.SentResponse> sendMail(@RequestBody EmailDTO.SentRequest emailRequest) {

        String authNum = EmailAuthService.createdCertifyNum();
        emailAuthService.saveVerificationCode(emailRequest.getTo(), authNum);
        EmailDTO.SentResponse response = emailService.sendEmailNotice(emailRequest, authNum, "email");

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

            Object attributionValue1 = session.getAttribute("emailVerified");
            Object attributionValue2 = session.getAttribute("verifiedEmail");

            if(attributionValue1 != null )
                log.info("session1 값이 존재합니다.");

            if(attributionValue2 != null)
                log.info("session2 값이 존재합니다.");


            return ResponseEntity.ok(EmailDTO.VerifyMailResponse.builder().status(true)
                    .message("이메일 인증이 성공적으로 완료되었습니다.")
                    .build());
        }


    }
}
