package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.EmailDTO;
import com.example.Sasimee_Back.repository.redis.RedisLockRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final RedisLockRepository redisLockRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmailNotice(EmailDTO.SentRequest emailRequest, String authNum, String type) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(emailRequest.getTo()); // 메일 수신자
        mimeMessageHelper.setSubject(emailRequest.getSubject()); // 메일 제목
        mimeMessageHelper.setText(setContext(authNum, "email"), true); // 메일 본문 내용, HTML 여부
        javaMailSender.send(mimeMessage);
    }

    // thymeleaf를 통한 html 적용, type에는 template의 이름이 들어감
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

    public void saveVerificationCode(String email, String verificationcode, long timeout) {
        redisLockRepository.save(email, verificationcode, timeout);
    }

    public void deleteVerificationCode(String email)
    {
        redisLockRepository.deleteData(email);
    }

    public void deleteVerificatedEmailInfo(String email)
    {
        redisLockRepository.deleteData(email);
    }

    public boolean verifyEmail(String email, String verificationCode)
    {
        String savedCode = redisLockRepository.getData(email);

        if(savedCode != null && savedCode.equals(verificationCode)) {
            redisLockRepository.deleteData(email);
            redisLockRepository.save(email, "true", 30);
            return true;
        }
        return false;
    }

    public boolean verifyEmailAuthentication(String email)
    {
        boolean isAuthenticated = redisLockRepository.existData(email);

        if(isAuthenticated) {
            return true;
        }
        else
            return false;
    }

    public boolean checkCertifiedNumIsMade(String key){
        return redisLockRepository.existKey(key);
    }

    public static String createdCertifyNum() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


}
