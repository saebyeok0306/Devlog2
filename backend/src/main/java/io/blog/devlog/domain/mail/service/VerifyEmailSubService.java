package io.blog.devlog.domain.mail.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.blog.devlog.domain.mail.types.MessageType;
import io.blog.devlog.global.redis.message.VerifyEmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyEmailSubService implements MessageListener {

    private final EmailService emailService;
    private final ObjectMapper mapper;

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        try {
            VerifyEmailMessage emailMessage = mapper.readValue(message.getBody(), VerifyEmailMessage.class);

            log.info("Received message from Redis");
            log.info("Email: {}", emailMessage.getEmail());
            log.info("Subject: {}", emailMessage.getSubject());
            log.info("Code: {}", emailMessage.getCode());

            HashMap<String, String> params = new HashMap<>();
            params.put("authCode", emailMessage.getCode());

            emailService.sendEmail(MessageType.EMAIL, emailMessage.getEmail(), emailMessage.getSubject(), params);

        } catch (Exception e) {
            log.error("onMessage error : {}", e.getMessage());
        }
    }
}
