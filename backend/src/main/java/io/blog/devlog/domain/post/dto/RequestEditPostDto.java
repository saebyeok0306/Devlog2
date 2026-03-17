package io.blog.devlog.domain.post.dto;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.file.dto.FileDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestEditPostDto {
    private Long id;
    private String url;
    private String title;
    private String content;
    private String previewUrl;
    private Long categoryId;
    @Builder.Default
    private List<FileDto> files = Collections.emptyList();
    private long views;
    private boolean hidden;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    public RequestEditPostDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public Post toEntity(User user, Category category) {
        Post newPost = Post.builder()
                .id(this.id)
                .url(this.url)
                .title(this.title)
                .content(this.content)
                .previewUrl(this.previewUrl)
                .category(category)
                .user(user)
                .views(this.views)
                .hidden(this.hidden)
                .build();
        newPost.setModifiedAt(this.modifiedAt);
        newPost.setCreatedAt(this.createdAt);
        return newPost;
    }
}
