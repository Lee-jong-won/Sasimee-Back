package com.example.Sasimee_Back.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> template;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public boolean existKey(String key){
        return template.hasKey(key);
    }

    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public boolean save(String key, String value, long timeout) {
        return template.opsForValue().setIfAbsent(key, value, Duration.ofMinutes(timeout));
    }

    public void deleteData(String key) {
        template.delete(key);
    }
}
