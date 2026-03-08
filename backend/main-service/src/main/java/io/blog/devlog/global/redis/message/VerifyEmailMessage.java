package io.blog.devlog.global.redis.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailMessage {
    private String email;
    private String subject;
    private String code;
}
