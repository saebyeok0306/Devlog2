package io.blog.devlog.global.redis.service;

import io.blog.devlog.global.redis.message.VerifyEmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyEmailPubService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void sendVerifyEmail(VerifyEmailMessage verifyEmailMessage) {
        redisTemplate.convertAndSend("VerifyEmail", verifyEmailMessage);
    }
}
