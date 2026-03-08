package io.blog.mail.mail.message

// val로 선언된 변수는 읽기 전용, var는 읽기/쓰기가 가능합니다.
data class AuthenticationMessage(val email: String, val subject: String, val authCode: String)
