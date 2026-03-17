package io.blog.devlog.domain.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationMessage {
    private String email;
    private String subject;
    private String authCode;
}
