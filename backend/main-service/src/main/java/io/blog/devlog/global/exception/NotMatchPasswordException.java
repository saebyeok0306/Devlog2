package io.blog.devlog.global.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException() {
        super();
    }

    public NotMatchPasswordException(String message) {
        super(message);
    }

    public NotMatchPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchPasswordException(Throwable cause) {
        super(cause);
    }
}
