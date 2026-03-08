package io.blog.devlog.global.redis.controller;

import io.blog.devlog.global.redis.message.VerifyEmailMessage;
import io.blog.devlog.global.redis.service.VerifyEmailPubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {
    private final VerifyEmailPubService verifyEmailPubService;

    @PostMapping("/verify-email")
    public String sendVerifyEmail(@RequestBody VerifyEmailMessage verifyEmailMessage) {
        verifyEmailPubService.sendVerifyEmail(verifyEmailMessage);
        return "success";
    }
}
