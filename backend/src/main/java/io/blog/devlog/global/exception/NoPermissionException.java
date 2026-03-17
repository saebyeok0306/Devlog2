package io.blog.devlog.global.exception;

public class NoPermissionException extends RuntimeException {
    /* 로그인은 했지만 권한이 부족한 경우 */
    public NoPermissionException() {
        super();
    }

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionException(Throwable cause) {
        super(cause);
    }
}
