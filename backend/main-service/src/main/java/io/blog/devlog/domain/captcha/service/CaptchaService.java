package io.blog.devlog.domain.captcha.service;

import io.blog.devlog.domain.captcha.dto.ResponseCaptchaDto;

public interface CaptchaService {
    public ResponseCaptchaDto verifyToken(String token);
}
