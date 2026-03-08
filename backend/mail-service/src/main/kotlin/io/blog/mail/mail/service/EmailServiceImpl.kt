package io.blog.mail.mail.service

import io.blog.mail.mail.types.MessageType
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.util.Random

@Service
class EmailServiceImpl : EmailService {
    @Autowired
    private lateinit var emailSender: JavaMailSender
    @Autowired
    private lateinit var templateEngine: SpringTemplateEngine

    override fun sendEmail(type: MessageType, email: String, subject: String, params: HashMap<String, String>) {
        val mimeMessage: MimeMessage = emailSender.createMimeMessage()

        try {
            val helper = MimeMessageHelper(mimeMessage, true, "utf-8")
            helper.setTo(email)
            helper.setSubject(subject)
            helper.setText(this.setContext(type, params), true)
            emailSender.send(mimeMessage)
        } catch (e : Exception) {
            println("Failed to send email $e")
        }
    }

    override fun setContext(type: MessageType, params: HashMap<String, String>): String {
        val context = Context()

        for (entry in params.entries) {
            context.setVariable(entry.key, entry.value)
        }

//        context.setVariable("authCode", authCode)
        return templateEngine.process(type.name.lowercase(), context)
    }
}