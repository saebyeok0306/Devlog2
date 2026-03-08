package io.blog.devlog.global.exception;

public class NullJwtException extends RuntimeException {
    /* JWT 토큰이 있어야 하는데, 비어있는 경우 */
    public NullJwtException() {
        super();
    }

    public NullJwtException(String message) {
        super(message);
    }

    public NullJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullJwtException(Throwable cause) {
        super(cause);
    }
}
