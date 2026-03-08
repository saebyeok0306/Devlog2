package io.blog.devlog.domain.user.controller;

import io.blog.devlog.domain.file.service.FileService;
import io.blog.devlog.domain.user.dto.RequestPasswordDto;
import io.blog.devlog.domain.user.dto.RequestProfileDto;
import io.blog.devlog.domain.user.dto.RequestProfileUrlDto;
import io.blog.devlog.domain.user.dto.ResponseUserProfileDto;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.domain.user.service.VerifyService;
import io.blog.devlog.global.exception.UnauthorizedAccessException;
import io.blog.devlog.global.redis.message.VerifyEmailMessage;
import io.blog.devlog.global.redis.service.VerifyEmailPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.blog.devlog.global.utils.SecurityUtils.getUserEmail;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final FileService fileService;
    private final UserService userService;
    private final VerifyService verifyService;
    private final VerifyEmailPubService verifyEmailPubService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    public ResponseEntity<ResponseUserProfileDto> getProfile() {
        String userEmail = getUserEmail();
        if (userEmail == null) throw new UnauthorizedAccessException("로그인이 필요합니다.");
        User user = userService.getUserByEmail(userEmail).orElseThrow(() -> new UnauthorizedAccessException("잘못된 요청입니다."));
        return ResponseEntity.ok(ResponseUserProfileDto.of(user));
    }

    @PostMapping
    public void updateProfile(@RequestBody RequestProfileDto requestProfileDto) {
        String userEmail = getUserEmail();
        if (userEmail == null) throw new UnauthorizedAccessException("로그인이 필요합니다.");
        User user = userService.getUserByEmail(userEmail).orElseThrow(() -> new UnauthorizedAccessException("잘못된 요청입니다."));
        user.setUsername(requestProfileDto.getUsername());
        user.setAbout(requestProfileDto.getAbout());
        user.setModifiedAt(LocalDateTime.now());
        userService.saveUser(user);
    }

    @PutMapping("/password")
    public void renewPassword(@RequestBody RequestPasswordDto requestPasswordDto) throws BadRequestException {
        log.info("PUT /password/renew RequestPasswordDto : {}", requestPasswordDto);

        if (requestPasswordDto.getCurrentPassword().isEmpty() || requestPasswordDto.getNewPassword().isEmpty()) {
            throw new BadRequestException("잘못된 요청입니다.");
        }
        if (requestPasswordDto.getCurrentPassword().equals(requestPasswordDto.getNewPassword())) {
            throw new BadRequestException("기존 비밀번호와 새 비밀번호가 같습니다.");
        }
        String userEmail = getUserEmail();
        if (userEmail == null) {
            throw new BadRequestException("잘못된 요청입니다.");
        }
        User user = userService.getUserByEmail(userEmail).orElseThrow(() -> new BadRequestException("잘못된 요청입니다."));

        user.updatePassword(bCryptPasswordEncoder, requestPasswordDto);
        userService.saveUser(user);
    }

    @GetMapping("/verify-email")
    public void requestVerifyEmail() {
        String email = getUserEmail();
        String code = verifyService.createVerifyCode(email);
        VerifyEmailMessage verifyEmailMessage = new VerifyEmailMessage(email, "[devLog] 이메일 인증 코드", code);

        verifyEmailPubService.sendVerifyEmail(verifyEmailMessage);
    }

    @PostMapping("/verify-email/{code}")
    public void sendVerifyEmail(@PathVariable String code) throws BadRequestException {
        String email = getUserEmail();
        if (!verifyService.checkVerifyCode(email, code)) {
            throw new BadRequestException("이메일 인증에 실패했습니다.");
        }

        User user = userService.getUserByEmail(email).orElseThrow(() -> new BadRequestException("잘못된 요청입니다."));
        user.certified();
        userService.saveUser(user);
    }

    @PostMapping("/picture")
    public void updateProfileUrl(@RequestBody RequestProfileUrlDto requestProfileUrlDto) throws BadRequestException {
        String email = getUserEmail();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new BadRequestException("잘못된 요청입니다."));
        fileService.getFileByUserId(user.getId()).ifPresent(
                file -> fileService.deleteFileFromUser(user)
        );
        fileService.uploadFileAndDeleteTempFile(user, List.of(requestProfileUrlDto.getFile()));
        fileService.deleteTempFiles();
        userService.updateProfileUrl(email, requestProfileUrlDto.getProfileUrl());
    }

    @DeleteMapping("/picture")
    public void deleteProfileUrl() throws BadRequestException {
        String email = getUserEmail();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new BadRequestException("잘못된 요청입니다."));
        fileService.getFileByUserId(user.getId()).ifPresent(
                file -> fileService.deleteFileFromUser(user)
        );
        userService.updateProfileUrl(email, null);
    }
}
