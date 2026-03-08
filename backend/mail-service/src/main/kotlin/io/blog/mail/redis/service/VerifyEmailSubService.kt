package io.blog.mail.redis.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.blog.mail.mail.service.EmailService
import io.blog.mail.mail.types.MessageType
import io.blog.mail.redis.message.VerifyEmailMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Service

@Service
class VerifyEmailSubService : MessageListener {
    @Autowired
    private lateinit var emailService: EmailService
    private val mapper = ObjectMapper()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val verifyEmailMessage = mapper.readValue(message.body, VerifyEmailMessage::class.java)

            println("Received message from Redis")
            println("Email: ${verifyEmailMessage.email}")
            println("Subject: ${verifyEmailMessage.subject}")
            println("Code: ${verifyEmailMessage.code}")

            val params = HashMap<String, String>()
            params["authCode"] = verifyEmailMessage.code

            emailService.sendEmail(MessageType.EMAIL, verifyEmailMessage.email, verifyEmailMessage.subject, params)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}