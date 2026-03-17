package io.blog.devlog.domain.mail.service;

import io.blog.devlog.domain.mail.types.MessageType;

import java.util.HashMap;

public interface EmailService {
    void sendEmail(MessageType type, String email, String subject, HashMap<String, String> params);
    String setContext(MessageType type, HashMap<String, String> params);
}
