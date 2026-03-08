package io.blog.gateway.authentication.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.security.Key;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class JwtService {

    private final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final String CLAIM_EMAIL = "email";
    private final String CLAIN_ROLE = "role";
    private final String ACCESS_TOKEN_COOKIE = "access_token";
    private final String REFRESH_TOKEN_COOKIE = "refresh_token";

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Optional<String> extractAccessToken(ServerHttpRequest request) {
        log.info("extractJWT() 호출");
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (!cookies.containsKey(ACCESS_TOKEN_COOKIE)) {
            return Optional.empty();
        }
        return cookies.get(ACCESS_TOKEN_COOKIE)
                .stream()
                .map(HttpCookie::getValue)
                .findFirst();
    }

    public Optional<String> extractRefreshToken(ServerHttpRequest request) {
        log.info("extractRefreshJWT() 호출");
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (!cookies.containsKey(REFRESH_TOKEN_COOKIE)) {
            return Optional.empty();
        }
        return cookies.get(REFRESH_TOKEN_COOKIE)
                .stream()
                .map(HttpCookie::getValue)
                .findFirst();
    }


    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("isTokenValid 호출 : {}", claims);
            return true;
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            String message = "유효하지 않은 토큰입니다.";
            log.error(message);
            throw new JwtException(message, e);
        } catch (ExpiredJwtException e) {
            String message = "만료된 JWT 토큰입니다.";
            log.error(message + "\n" + e.getMessage());
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), message);
        } catch (UnsupportedJwtException e) {
            String message = "지원되지 않는 JWT 토큰입니다.";
            log.error(message);
            throw new JwtException(message, e);
        } catch (Exception e) {
            String message = String.format("유효하지 않은 토큰입니다. %s", e.getMessage());
            log.error(message);
            throw new JwtException(message, e);
        }
    }

    public boolean isAccessTokenValid(String token) {
        Claims claims = this.extractClaims(token);
        if (!Objects.equals(claims.getSubject(), ACCESS_TOKEN_SUBJECT)) {
            String message = "AccessToken이 아닙니다.";
            log.error(message);
            return false;
        }
        return true;
    }

    public boolean isRefreshTokenValid(String token) {
        log.info("isRefreshTokenValid 호출");
        Claims claims = this.extractClaims(token);
        if (!Objects.equals(claims.getSubject(), REFRESH_TOKEN_SUBJECT)) {
            String message = "RefreshToken이 아닙니다.";
            log.error(message);
            return false;
        }
        return true;
    }
}
