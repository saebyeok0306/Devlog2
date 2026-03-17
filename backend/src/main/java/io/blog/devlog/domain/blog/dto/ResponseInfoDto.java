package io.blog.devlog.domain.blog.dto;

import io.blog.devlog.domain.blog.model.Blog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInfoDto {
    private String username;
    private String about; // 블로그 소개글
    private String profileUrl; // 블로그 프로필 사진

    public static ResponseInfoDto toDto(Blog blog) {
        return ResponseInfoDto.builder()
                .username(blog.getUser().getUsername())
                .about(blog.getAbout())
                .profileUrl(blog.getProfileUrl())
                .build();
    }
}
