package io.blog.devlog.domain.mail.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.blog.devlog.domain.mail.types.MessageType;
import io.blog.devlog.global.redis.message.CommentEmailMessage;
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
public class CommentEmailSubService implements MessageListener {

    private final EmailService emailService;
    private final ObjectMapper mapper;

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        try {
            CommentEmailMessage emailMessage = mapper.readValue(message.getBody(), CommentEmailMessage.class);

            log.info("Received message from Redis");
            log.info("Email: {}", emailMessage.getEmail());
            log.info("Subject: {}", emailMessage.getSubject());
            log.info("postName: {}", emailMessage.getPostName());

            HashMap<String, String> params = new HashMap<>();
            params.put("postName", emailMessage.getPostName());
            params.put("postUrl", emailMessage.getPostUrl());
            params.put("commentContent", emailMessage.getCommentContent());
            params.put("commentAuthor", emailMessage.getCommentAuthor());

            emailService.sendEmail(MessageType.COMMENT, emailMessage.getEmail(), emailMessage.getSubject(), params);

        } catch (Exception e) {
            log.error("onMessage error : {}", e.getMessage());
        }
    }
}
