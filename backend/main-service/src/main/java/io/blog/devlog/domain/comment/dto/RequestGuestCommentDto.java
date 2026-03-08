package io.blog.devlog.domain.comment.dto;

import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.comment.model.GuestDetail;
import io.blog.devlog.domain.file.dto.FileDto;
import io.blog.devlog.domain.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestGuestCommentDto {
    private String postUrl;
    private Long parent; // 대댓글
    private String content;
    @ColumnDefault("false")
    private boolean hidden;

    private String username;
    private String password;

    public Comment toEntity(BCryptPasswordEncoder bCryptPasswordEncoder, Post post) {
        GuestDetail guest = GuestDetail.builder()
                .username(this.username)
                .password(bCryptPasswordEncoder.encode(this.password))
                .build();
        return Comment.builder()
                .user(null)
                .guest(guest)
                .post(post)
                .parent(this.parent)
                .content(this.content)
                .hidden(this.hidden)
                .build();
    }
}
