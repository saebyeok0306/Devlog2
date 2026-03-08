package io.blog.gateway.authentication.filter;

import io.blog.gateway.authentication.service.JwtService;
import io.blog.gateway.global.response.ErrorResponse;
import io.blog.gateway.status.CustomStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationProcessingFilter {

    private final JwtService jwtService;
    private final ErrorResponse errorResponse;

    public Mono<Void> doFilterInternal(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        Optional<String> extractAccessToken = jwtService.extractAccessToken(request);
        if (extractAccessToken.isPresent()) {
            try {
                jwtService.isTokenValid(extractAccessToken.get());
            }
            catch (ExpiredJwtException e) {
                return errorResponse.setResponse(exchange, CustomStatus.EXPIRED_JWT.getValue(), e.getMessage());
            }
            catch (JwtException e) {
                return errorResponse.setResponse(exchange, CustomStatus.INVALID_JWT.getValue(), e.getMessage());
            }
        }


        return null;
    }
}
