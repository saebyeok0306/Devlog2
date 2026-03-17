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
public class ResponsePostNonContentDto {
    private String url;
    private String title;
    private String previewUrl;
    private ResponseUserDto user;
    private CategoryDto category;
    private long views;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    public static ResponsePostNonContentDto of(Post post) {
        return ResponsePostNonContentDto.builder()
                .url(post.getUrl())
                .title(post.getTitle())
                .previewUrl(post.getPreviewUrl())
                .user(ResponseUserDto.of(post.getUser()))
                .category(CategoryDto.of(post.getCategory()))
                .views(post.getViews())
                .modifiedAt(post.getModifiedAt())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
