package io.blog.devlog.domain.mail.controller;

import io.blog.devlog.domain.mail.dto.AuthenticationMessage;
import io.blog.devlog.domain.mail.service.EmailService;
import io.blog.devlog.domain.mail.types.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    /* TEST CODE */
    @PostMapping("/send")
    void sendEmail(@RequestBody AuthenticationMessage message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("authCode", message.getAuthCode());
        emailService.sendEmail(MessageType.EMAIL, message.getEmail(), message.getSubject(), params);
    }
}
