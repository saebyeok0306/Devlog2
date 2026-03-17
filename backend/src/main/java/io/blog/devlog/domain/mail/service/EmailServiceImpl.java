package io.blog.devlog.domain.mail.service;

import io.blog.devlog.domain.mail.types.MessageType;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(MessageType type, String email, String subject, HashMap<String, String> params) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(this.setContext(type, params), true);
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    @Override
    public String setContext(MessageType type, HashMap<String, String> params) {
        Context context = new Context();

        for (String key : params.keySet()) {
            context.setVariable(key, params.get(key));
        }

        return templateEngine.process(type.name().toLowerCase(), context);
    }
}
