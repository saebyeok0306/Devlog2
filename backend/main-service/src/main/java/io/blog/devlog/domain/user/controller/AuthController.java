package io.blog.devlog.domain.user.controller;

import io.blog.devlog.domain.user.dto.UserDto;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final SuccessResponse successResponse;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public void join(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody UserDto userDto) throws IOException {
        log.info("POST /join UserDto : {}", userDto);

        Optional<User> userEntity = userService.getUserByEmail(userDto.getEmail());
        if (userEntity.isPresent()) {
            throw new BadRequestException("이미 가입된 이메일 계정입니다.");
        }

        if(userDto.getUsername().isEmpty() || userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty()) {
            throw new BadRequestException("잘못된 입력 정보입니다.");
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .certificate(false) // 인증이 안된 상태
                .role(Role.USER)
                .build();

        user.passwordEncode(bCryptPasswordEncoder);
        userService.saveUser(user);

        Integer status = HttpServletResponse.SC_OK;
        String message = "가입 성공";
        successResponse.setResponse(response, status, message, request.getRequestURI());
    }

    @GetMapping("/check")
    public void check() {
        log.info("GET /check");
    }

    @GetMapping("/jwt")
    public ResponseEntity<Boolean> hasJwtCookie(HttpServletRequest request) {
        boolean check = userService.hasJwtCookie(request);
        return ResponseEntity.ok(check);
    }

    @GetMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        userService.reissueToken(request, response);
    }

    @GetMapping("/signout")
    public void logout(HttpServletResponse response) {
        log.info("GET /signout");
        userService.logout(response);
    }
}
