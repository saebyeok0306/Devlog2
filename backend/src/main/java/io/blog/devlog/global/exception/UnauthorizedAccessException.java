package io.blog.devlog.global.exception;

public class UnauthorizedAccessException extends RuntimeException {
    /* 로그인 없이 인증이 필요한 서비스에 접근한 경우 */
    public UnauthorizedAccessException() {
        super();
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedAccessException(Throwable cause) {
        super(cause);
    }
}
