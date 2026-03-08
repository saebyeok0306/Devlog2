package io.blog.devlog.domain.user.service;

import io.blog.devlog.config.TestConfig;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.repository.UserRepository;
import io.blog.devlog.global.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserService2Test {
    @Autowired
    private UserRepository userRepository;
    private JwtService jwtService;
    private UserService userService;
    private static final TestConfig testConfig = new TestConfig();

    @BeforeEach
    public void beforeSetUp() {
        jwtService = new TestConfig().createJwtService();
        userService = new UserService(userRepository, jwtService);
    }
    
    @Test
    @DisplayName("Reissue Token 검증")
    public void reissueAccessTokenTest() {
        // given
        System.out.println(userRepository.findAll());
        User testUser = userRepository.save(testConfig.adminUser);
        String refreshToken = jwtService.createRefreshToken(testUser);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/reissue");
//        request.addHeader("Authorization", "Bearer " + refreshToken);
        request.setCookies(jwtService.createRefreshTokenCookie(refreshToken));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        userService.reissueToken(request, response);

        // then
        Cookie accessTokenCookie = response.getCookie("access_token");
        Assertions.assertThat(accessTokenCookie).isNotNull();
        String accessToken = accessTokenCookie.getValue();
        Assertions.assertThat(accessToken).isNotNull();
        Assertions.assertThat(jwtService.isTokenValid(accessToken)).isTrue();
        Claims claims = jwtService.extractClaims(accessToken);
        Assertions.assertThat(claims.get("email")).isEqualTo(testConfig.email);
    }

    @Test
    @DisplayName("ADMIN 계정 가져오기 검증")
    public void getAdminTest() {
        // given
        userRepository.save(testConfig.adminUser);
        User guestUser = User.builder()
                .email("test2@naver.com")
                .password(testConfig.password)
                .username(testConfig.username)
                .role(Role.GUEST)
                .certificate(true)
                .build();
        userRepository.save(guestUser);

        // when
        User admin = userService.getAdmin();

        // then
        Assertions.assertThat(admin).isNotNull();
        Assertions.assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
    }
}
