package io.blog.mail.redis.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.blog.mail.mail.service.EmailService
import io.blog.mail.mail.types.MessageType
import io.blog.mail.redis.message.CommentEmailMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Service

@Service
class CommentEmailSubService : MessageListener {
    @Autowired
    private lateinit var emailService: EmailService
    private val mapper = ObjectMapper()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val emailMessage = mapper.readValue(message.body, CommentEmailMessage::class.java)

            val params = HashMap<String, String>()
            params["postName"] = emailMessage.postName
            params["postUrl"] = emailMessage.postUrl
            params["commentContent"] = emailMessage.commentContent
            params["commentAuthor"] = emailMessage.commentAuthor

            emailService.sendEmail(MessageType.COMMENT, emailMessage.email, emailMessage.subject, params)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}