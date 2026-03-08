package io.blog.devlog.domain.sitemap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUrlDto {
    private Long categoryId;
    private String url;

    public static PostUrlDto of(Long categoryId, String url) {
        return PostUrlDto.builder()
                .categoryId(categoryId)
                .url(url)
                .build();
    }
}
