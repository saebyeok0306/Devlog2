package io.blog.devlog.config;

import io.blog.devlog.domain.file.handler.FileHandler;
import io.blog.devlog.domain.file.service.TempFileService;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.jwt.service.JwtService;
import io.blog.devlog.global.login.dto.PrincipalDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

public class TestConfig {
    public String username = "admin";
    public String password = "password";
    public String email = "test@gmail.com";
    public Role role = Role.ADMIN;
    public String guestUsername = "guest";
    public String guestPassword = "password";
    public String guestEmail = "guest@gmail.com";
    public Role guestRole = Role.GUEST;
    public User adminUser = User.builder()
            .email(email)
            .password(password)
            .username(username)
            .role(role)
            .certificate(true)
            .build();
    public User guestUser = User.builder()
            .email(guestEmail)
            .password(guestPassword)
            .username(guestUsername)
            .role(guestRole)
            .certificate(true)
            .build();

    public JwtService createJwtService() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "abcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgab");
        ReflectionTestUtils.setField(jwtService, "accessHeader", "Authorization");
        ReflectionTestUtils.setField(jwtService, "refreshHeader", "Authorization-Refresh");
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 3600);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", 86400);
        ReflectionTestUtils.setField(jwtService, "frontendDomain", "localhost");
        jwtService.init();
        return jwtService;
    }

    public FileHandler createFileHandler(TempFileService tempFileService) {
        FileHandler fileHandler = new FileHandler(tempFileService);
        ReflectionTestUtils.setField(fileHandler, "uploadPath", "C:\\Programming\\Blog\\devlog\\backend\\src\\main\\resources\\upload");
        ReflectionTestUtils.setField(fileHandler, "requestPath", "res");
        return fileHandler;
    }

    public void updateAuthentication(User user) {
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails,
                null,
                principalDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}
