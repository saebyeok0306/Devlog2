package io.blog.devlog.domain.post.dto;

import io.blog.devlog.domain.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostUrlDto {
    private Long categoryId;
    private String url;

    public static ResponsePostUrlDto of(Long categoryId, String url) {
        return ResponsePostUrlDto.builder()
                .categoryId(categoryId)
                .url(url)
                .build();
    }
}
