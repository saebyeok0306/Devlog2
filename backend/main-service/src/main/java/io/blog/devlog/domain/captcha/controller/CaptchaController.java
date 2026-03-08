package io.blog.devlog.domain.captcha.controller;

import io.blog.devlog.domain.captcha.dto.RequestCaptchaDto;
import io.blog.devlog.domain.captcha.dto.ResponseCaptchaDto;
import io.blog.devlog.domain.captcha.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    private final CaptchaService captchaService;

    @PostMapping("/verify")
    public ResponseCaptchaDto verifyCaptcha(@RequestBody RequestCaptchaDto captchaMessage) {
        return captchaService.verifyToken(captchaMessage.getToken());
    }
}
