package io.blog.devlog.domain.user.dto;

import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserProfileDto {
    private String username;
    private String email;
    private String about;
    private String profileUrl;
    private Role role;
    private String provider;
    private Boolean certificate;

    public static ResponseUserProfileDto of(User user) {
        return ResponseUserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .about(user.getAbout())
                .profileUrl(user.getProfileUrl())
                .role(user.getRole())
                .provider(user.getProvider())
                .certificate(user.getCertificate())
                .build();
    }
}
