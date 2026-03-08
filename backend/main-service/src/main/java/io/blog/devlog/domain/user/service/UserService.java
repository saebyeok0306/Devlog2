package io.blog.devlog.domain.user.service;

import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.repository.UserRepository;
import io.blog.devlog.global.exception.InvalidJwtException;
import io.blog.devlog.global.exception.NullJwtException;
import io.blog.devlog.global.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean hasJwtCookie(HttpServletRequest request) {
        log.info("check Jwt");
        String token = jwtService.extractRefreshJWT(request).orElse(null);
        return token != null;
    }

    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("reissueRequest : " + request.toString());
        try {
            String refreshToken = jwtService.extractRefreshJWT(request).orElse(null);
            if (refreshToken == null) {
                log.error("refreshToken is null");
                throw new NullJwtException("refreshToken is null");
            }
            if (jwtService.isTokenValid(refreshToken) && jwtService.isRefreshTokenValid(refreshToken)) {
                Claims claims = jwtService.extractClaims(refreshToken);
                User user = userRepository.findByEmail(jwtService.get_claim_email(claims)).orElse(null);
                if (user == null) throw new InvalidJwtException("Invalid refresh token");
                String newAccessToken = jwtService.createAccessToken(user);
                // TODO: 여기서 refreshToken도 같이 재발급 받아서 넘겨주면, autoLogin 구현?
                jwtService.sendAccessToken(response, newAccessToken);
            }
        } catch (JwtException e) {
            throw new InvalidJwtException("Invalid refresh token");
        }
    }

    public void logout(HttpServletResponse response) {
        jwtService.expireTokenCookie(response);
    }

    public User getAdmin() {
        List<User> users = userRepository.findAllByRole(Role.ADMIN);
        if (users.isEmpty()) return null;
        return users.get(0);
    }

    public void updateProfileUrl(String email, String profileUrl) {
        userRepository.updateProfileUrl(email, profileUrl);
    }
}
