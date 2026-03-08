package io.blog.mail.redis.message

import com.fasterxml.jackson.annotation.JsonProperty

data class VerifyEmailMessage(
        @JsonProperty("email") val email: String,
        @JsonProperty("subject") val subject: String,
        @JsonProperty("code") val code: String
)