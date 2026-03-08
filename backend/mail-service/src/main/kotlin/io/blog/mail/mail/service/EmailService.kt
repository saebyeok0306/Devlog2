package io.blog.mail.mail.service

import io.blog.mail.mail.types.MessageType

interface EmailService {
    fun sendEmail(type: MessageType, email: String, subject: String, params: HashMap<String, String>)
    fun setContext(type: MessageType, params: HashMap<String, String>) : String
}