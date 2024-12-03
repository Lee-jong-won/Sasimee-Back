package com.example.Sasimee_Back.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Random;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepository {

    private final RedisTemplate<String, String> template;

    @Value("${spring.data.redis.duration}")
    private int duration;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key, String value) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        template.delete(key);
    }

    public void createRedisData(String toEmail, String code) {
        if (existData(toEmail)) {
            deleteData(toEmail);
        }

        setDataExpire(toEmail, code);
    }
}
