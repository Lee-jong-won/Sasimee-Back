package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.EmailDTO;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    @Async
    public EmailDTO.SentResponse sendEmailNotice(EmailDTO.SentRequest emailRequest, String authNum, String type){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailRequest.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailRequest.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext(authNum, "email"), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Succeeded to send Email");

            return EmailDTO.SentResponse.builder()
                    .status(true)
                    .message("Email sent successfully.")
                    .receiver(emailRequest.getTo())
                    .build();

        } catch (Exception e) {
            log.info("message={}", e);
            log.info("Failed to send Email");

            return EmailDTO.SentResponse.builder()
                    .status(false)
                    .message("Email doesn't sent successfully.")
                    .receiver(emailRequest.getTo())
                    .build();
        }
    }

    // thymeleaf를 통한 html 적용, type에는 template의 이름이 들어감
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
}
