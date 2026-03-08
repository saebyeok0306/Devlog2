package io.blog.devlog.domain.post.dto;

import io.blog.devlog.domain.category.dto.CategoryDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.dto.ResponseUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostDto {
    private Long id;
    private String url;
    private String title;
    private String content;
    private String previewUrl;
    private ResponseUserDto user;
    private CategoryDto category;
    private long views;
    private boolean hidden;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;
    private boolean ownership; // 권한 소유

    public static ResponsePostDto of(String email, Post post) {
        return ResponsePostDto.builder()
                .id(post.getId())
                .url(post.getUrl())
                .title(post.getTitle())
                .content(post.getContent())
                .previewUrl(post.getPreviewUrl())
                .user(ResponseUserDto.of(post.getUser()))
                .category(CategoryDto.of(post.getCategory()))
                .views(post.getViews())
                .hidden(post.isHidden())
                .modifiedAt(post.getModifiedAt())
                .createdAt(post.getCreatedAt())
                .ownership(post.getUser().getEmail().equals(email))
                .build();
    }
}
