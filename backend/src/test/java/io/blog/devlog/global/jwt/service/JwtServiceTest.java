package io.blog.devlog.global.jwt.service;

import io.blog.devlog.config.TestConfig;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

@DataJpaTest // Component Scan을 하지 않아 컨테이너에 @Component 빈들이 등록되지 않는다.
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class JwtServiceTest {

    @Autowired
    private UserRepository userRepository;
    private static JwtService jwtService;
    private static BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final long pastTime = 826038000; // 1996년 3월 6일
    private static final TestConfig testConfig = new TestConfig();

    @BeforeAll
    public static void beforeAllSetUp() {
        jwtService = testConfig.createJwtService();
    }

    @Test
    @DisplayName("AccessToken 검증")
    public void jwtTest1() {
        // given
        userRepository.save(testConfig.adminUser);

        // when
        String token = jwtService.createAccessToken(testConfig.adminUser);
        System.out.println("token : " + token);

        // then
        Assertions.assertThat(jwtService.isTokenValid(token)).isTrue();
        String email = jwtService.extractEmail(token).orElse(null);
        Assertions.assertThat(email).isNotEqualTo(null);
        Assertions.assertThat(email).isEqualTo(testConfig.email);
    }

    @Test
    @DisplayName("RefreshToken 검증")
    public void jwtTest2() {
        // given
        userRepository.save(testConfig.adminUser);

        // when
        String token = jwtService.createRefreshToken(testConfig.adminUser);
        System.out.println("token : " + token);

        // then
        Assertions.assertThat(jwtService.isTokenValid(token)).isTrue();
        String email = jwtService.extractEmail(token).orElse(null);
        Assertions.assertThat(email).isNotEqualTo(null);
        Assertions.assertThat(email).isEqualTo(testConfig.email);
    }

    @Test
    @DisplayName("isTokenValid 검증 - 유효하지 않은 JWT")
    public void isTokenValidTest1() {
        // given
        String token = "123";

        // when

        // then
        Assertions.assertThatThrownBy(() -> jwtService.isTokenValid(token)).isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("isTokenValid 검증 - 만료된 토큰")
    public void isTokenValidTest2() {
        // given
        userRepository.save(testConfig.adminUser);

        // when
        String token = jwtService.createRefreshToken(testConfig.adminUser, new Date(pastTime));

        // then
        Assertions.assertThatThrownBy(() -> jwtService.isTokenValid(token)).isInstanceOf(ExpiredJwtException.class);
    }
}
