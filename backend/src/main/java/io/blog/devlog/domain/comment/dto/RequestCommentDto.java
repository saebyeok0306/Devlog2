package io.blog.devlog.domain.comment.dto;

import io.blog.devlog.domain.comment.model.Comment;
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
@ToString
public class RequestCommentDto {
    private String postUrl;
    private Long parent; // 대댓글
    private String content;
    @Builder.Default
    private List<FileDto> files = Collections.emptyList();
    @ColumnDefault("false")
    private boolean hidden;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .parent(this.parent)
                .content(this.content)
                .hidden(this.hidden)
                .build();
    }
}
