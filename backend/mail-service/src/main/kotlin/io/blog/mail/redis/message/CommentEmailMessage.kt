package io.blog.mail.redis.message

import com.fasterxml.jackson.annotation.JsonProperty

data class CommentEmailMessage(
        @JsonProperty("email") val email: String,
        @JsonProperty("subject") val subject: String,
        @JsonProperty("postName") val postName: String,
        @JsonProperty("postUrl") val postUrl: String,
        @JsonProperty("commentContent") val commentContent: String,
        @JsonProperty("commentAuthor") val commentAuthor: String
)