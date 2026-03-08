package io.blog.devlog.domain.post.dto;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.file.dto.FileDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPostDto {
    private String url;
    private String title;
    private String content;
    private String previewUrl;
    private Long categoryId;
    @Builder.Default
    private List<FileDto> files = Collections.emptyList();
    @ColumnDefault("false")
    private boolean hidden;

    public RequestPostDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public Post toEntity(User user, Category category) {
        return Post.builder()
                .url(this.url)
                .title(this.title)
                .content(this.content)
                .previewUrl(this.previewUrl)
                .category(category)
                .user(user)
                .hidden(this.hidden)
                .build();
    }
}
