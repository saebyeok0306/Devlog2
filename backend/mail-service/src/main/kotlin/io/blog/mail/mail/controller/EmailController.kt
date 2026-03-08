package io.blog.mail.mail.controller

import io.blog.mail.mail.message.AuthenticationMessage
import io.blog.mail.mail.service.EmailService
import io.blog.mail.mail.types.MessageType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController {
    @Autowired
    private lateinit var emailService : EmailService

    @PostMapping("/send")
    fun sendEmail(@RequestBody message: AuthenticationMessage) {
        val params = HashMap<String, String>()
        params["authCode"] = message.authCode
        emailService.sendEmail(MessageType.EMAIL, message.email, message.subject, params)
    }
}