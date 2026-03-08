package io.blog.devlog.global.oauth2.handler;

import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.jwt.service.JwtService;
import io.blog.devlog.global.login.dto.PrincipalDetails;
import io.blog.devlog.global.response.SuccessResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final SuccessResponse successResponse;
    private final UserService userService;
    private final JwtService jwtService;

    @Value("${frontend.url}")
    private String frontendOriginUrl;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();
            if (loginSuccess(response, oAuth2User)) {
                // 로그인에 성공한 경우 access, refresh 토큰 생성
                return;
            }
        } catch (Exception e) {
            throw e;
        }

        Integer status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = "소셜 로그인에 실패했습니다.";
        successResponse.setResponse(response, status, message, request.getRequestURI());
    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private boolean loginSuccess(HttpServletResponse response, PrincipalDetails oAuth2User) throws IOException {
        String email = oAuth2User.getUsername();
        User user = userService.getUserByEmail(email).orElse(null);
        if(user == null) {
            log.error("로그인한 OAuth 유저의 정보가 없습니다! email : {}", email);
            return false;
        }
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
//        response.addHeader(jwtService.getAccessHeader(), jwtService.getBEARER() + accessToken);
//        response.addHeader(jwtService.getRefreshHeader(), jwtService.getBEARER() + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        userService.updateRefreshToken(user, refreshToken);
//        user.updateRefreshToken(refreshToken);

        response.sendRedirect(String.format("%s/callback", frontendOriginUrl));
//        response.sendRedirect(String.format("%s/callback?at=%s&rt=%s", frontendOriginUrl, accessToken, refreshToken));
        return true;
    }
}
