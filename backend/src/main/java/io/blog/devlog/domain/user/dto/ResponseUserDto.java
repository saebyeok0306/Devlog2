package io.blog.devlog.domain.user.dto;

import io.blog.devlog.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {
    private String username;
    private String email;
    private String about;
    private String profileUrl;

    public static ResponseUserDto of(User user) {
        return ResponseUserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .about(user.getAbout())
                .profileUrl(user.getProfileUrl())
                .build();
    }

    public static ResponseUserDto of(String username) {
        return ResponseUserDto.builder()
                .username(username)
                .email(null)
                .about(null)
                .profileUrl(null)
                .build();
    }
}
