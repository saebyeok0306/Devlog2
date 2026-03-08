package io.blog.devlog.global.exception;

public class InvalidJwtException extends RuntimeException {
    /* 잘못된 JWT 토큰인 경우 */
    public InvalidJwtException() {
        super();
    }

    public InvalidJwtException(String message) {
        super(message);
    }

    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtException(Throwable cause) {
        super(cause);
    }
}
