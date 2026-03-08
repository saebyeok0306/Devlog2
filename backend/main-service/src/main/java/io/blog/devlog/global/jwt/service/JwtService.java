package io.blog.devlog.global.jwt.service;

import io.blog.devlog.domain.user.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class JwtService {

    @Getter
    private final String BEARER = "Bearer ";
    private final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final String CLAIM_NAME = "username";
    private final String CLAIM_EMAIL = "email";
    private final String CLAIN_ROLE = "role";
    private final String ACCESS_TOKEN_COOKIE = "access_token";
    private final String REFRESH_TOKEN_COOKIE = "refresh_token";

    @Value("${jwt.secret}")
    private String secret;
    @Getter
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Getter
    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;
    @Value("${frontend.domain}")
    private String frontendDomain;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String get_claim_email(Claims claims) {
        return (String) claims.get(CLAIM_EMAIL);
    }

    public String createAccessToken(User user) {
        // 토큰의 expire 시간을 설정
        Date date = new Date();
        long now = date.getTime();
        Date validity = new Date(now + accessTokenExpiration*1000);

        // JWT 용어가 헷갈릴 수 있는데, 내용 정리를 하자면
        // JWT는 Header, Payload, Signature로 이루어져 있음.
        // 의존성으로 추가한 JWT 라이브러리에서 Header는 알아서 생성해주고,
        // Payload와 Signature만 설정하면 된다.
        // 이때, Playload에 담겨 있는 데이터 조각들을 Claim이라고 부른다.
        // 이러한 Claim에서 이미 지정된 표현들이 있는데(Registered Claim),
        // iss (토큰발급자, issuer), sub (토큰제목, subject), aud (토큰대상자, audience)
        // exp (토큰만료시간, expiration), nbf (토큰활성날짜, not before), iat (토큰발급시간, issued at),
        // jti (토큰식별자, JWT ID)가 있다.
        // Jwts.builder()에서 이러한 예약데이터들은 메소드로 존재하고 그 외에 추가로 넣고 싶으면
        // claim 메소드를 통해 새롭게 추가할 수 있는 개념.
        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT) // Jwt Subject
                .claim(CLAIM_EMAIL, user.getEmail())
                .setExpiration(validity) // set Expire Time 해당 옵션 안넣으면 expire안함
                .setIssuedAt(date)
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 , signature 에 들어갈 secret값 세팅
                .compact();
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken(User user) {
        Date date = new Date();
        long now = date.getTime();
        Date validity = new Date(now + refreshTokenExpiration*1000);

        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .claim(CLAIM_EMAIL, user.getEmail())
                .setExpiration(validity)
                .setIssuedAt(date)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(User user, Date date) {
        long now = date.getTime();
        Date validity = new Date(now + refreshTokenExpiration*1000);

        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .claim(CLAIM_EMAIL, user.getEmail())
                .setExpiration(validity)
                .setIssuedAt(date)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Cookie createAccessTokenCookie(String accessToken) {
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE, accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setDomain(frontendDomain);
        return accessTokenCookie;
    }

    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setDomain(frontendDomain);
        refreshTokenCookie.setMaxAge((int) refreshTokenExpiration);
        return refreshTokenCookie;
    }

    public void expireTokenCookie(HttpServletResponse response) {
        Cookie accessCookie = createAccessTokenCookie("");
        Cookie refreshCookie = createRefreshTokenCookie("");
        accessCookie.setMaxAge(0);
        refreshCookie.setMaxAge(0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addCookie(this.createAccessTokenCookie(accessToken));
    }

    public void sendRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addCookie(this.createRefreshTokenCookie(refreshToken));
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        this.sendAccessToken(response, accessToken);
        this.sendRefreshToken(response, refreshToken);
    }

    public Optional<String> extractJWT(HttpServletRequest request) {
        log.info("extractJWT() 호출");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN_COOKIE))
                .map(Cookie::getValue)
                .findFirst();
    }

    public Optional<String> extractRefreshJWT(HttpServletRequest request) {
        log.info("extractRefreshJWT() 호출");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE))
                .map(Cookie::getValue)
                .findFirst();
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            log.info("extractUsername : {}", claims.get(CLAIM_EMAIL));
            return Optional.ofNullable((String) claims.get(CLAIM_EMAIL));
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
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
