package com.example.miniproject.service;

import com.example.miniproject.dto.http.ResponseMessage;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void setValues(String token, String userId) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, userId);
    }

    public String getValues(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    public void deleteValues(String token) {
        if(redisTemplate.delete(token)) {
            return;
        } else {
            throw new CustomException(ResponseMessage.LOGOUT_FAIL, StatusCode.BAD_REQUEST);
        }
    }
}
