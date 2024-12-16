package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.repository.redis.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailVerificationRepository emailVerificationRepository;
    private static final String VERIFICATION_CODE_FEY_PREFIX = "verification-code:";
    private static final String VERIFICATED_EMAIL_INFO = "verified-email:";
    public void saveVerificationCode(String email, String verificationcode, long timeout) {

        String key = VERIFICATION_CODE_FEY_PREFIX + email;

        if (emailVerificationRepository.existData(key)) {
            emailVerificationRepository.deleteData(key);
        }

        emailVerificationRepository.save(key, verificationcode, timeout);
    }

    public void deleteVerificationCode(String email)
    {
        String key = VERIFICATION_CODE_FEY_PREFIX + email;
        emailVerificationRepository.deleteData(key);
    }

    public void deleteVerificatedEmailInfo(String email)
    {
        String key = VERIFICATED_EMAIL_INFO + email;
        emailVerificationRepository.deleteData(key);
    }

    public boolean verifyEmail(String email, String verificationCode)
    {
        String key = VERIFICATION_CODE_FEY_PREFIX + email;
        String savedCode = emailVerificationRepository.getData(key);

        if(savedCode != null && savedCode.equals(verificationCode)) {
            emailVerificationRepository.deleteData(key);

            String emailkey = VERIFICATED_EMAIL_INFO + email;
            emailVerificationRepository.save(emailkey, "true", 30);

            return true;
        }
        return false;
    }

    public boolean verifyEmailAuthentication(String email)
    {
        String emailkey = VERIFICATED_EMAIL_INFO + email;
        boolean isAuthenticated = emailVerificationRepository.existData(emailkey);

        if(isAuthenticated) {
            return true;
        }
        else
            return false;
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
