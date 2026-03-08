package io.blog.devlog.global.exception;

import io.blog.devlog.global.response.ErrorResponse;
import io.blog.devlog.global.status.CustomStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorResponse errorResponse;
    @ExceptionHandler(BadRequestException.class)
    public void badRequestExceptionHandler(BadRequestException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = HttpServletResponse.SC_BAD_REQUEST;
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentExceptionHandler(IllegalArgumentException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = HttpServletResponse.SC_BAD_REQUEST;
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(NullJwtException.class)
    public void nullJwtExceptionHandler(NullJwtException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = CustomStatus.INVALID_JWT.getValue();
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public void invalidJwtExceptionHandler(InvalidJwtException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = CustomStatus.INVALID_JWT.getValue();
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public void notMatchPasswordExceptionHandler(NotMatchPasswordException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = HttpServletResponse.SC_BAD_REQUEST;
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(NotFoundException.class)
    public void notFoundExceptionHandler(NotFoundException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = HttpServletResponse.SC_NOT_FOUND;
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(NoPermissionException.class)
    public void noPermissionExceptionHandler(NoPermissionException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = HttpServletResponse.SC_FORBIDDEN;
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public void unauthorizedAccessExceptionHandler(UnauthorizedAccessException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = HttpServletResponse.SC_UNAUTHORIZED;
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public void internalServerErrorExceptionHandler(InternalServerErrorException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String error = e.getMessage();
        String path = request.getRequestURI();
        errorResponse.setResponse(response, status, error, path);
    }
}
