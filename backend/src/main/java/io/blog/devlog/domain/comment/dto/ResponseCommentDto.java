package io.blog.devlog.domain.comment.dto;

import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.dto.ResponseUserDto;
import io.blog.devlog.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentDto {
    private Long id;
    private ResponseUserDto user;
    private Long parent; // 대댓글
    private String content;
    private boolean hidden; // 비밀댓글 체크 여부
    private boolean hiddenFlag; // 현재 사용자가 댓글을 볼 수 없는지 여부
    private boolean deleted;
    private LocalDateTime createdAt;
    private boolean ownership; // 권한 소유

    public static ResponseCommentDto of(Comment comment, Post post, User user) {
        if (comment.isDeleted()) {
            return ResponseCommentDto.builder()
                    .id(comment.getId())
                    .user(null)
                    .parent(comment.getParent())
                    .content(null)
                    .createdAt(comment.getCreatedAt())
                    .hidden(false)
                    .hiddenFlag(false)
                    .deleted(comment.isDeleted())
                    .ownership(false)
                    .build();
        }

        String email = user != null ? user.getEmail() : null;
        boolean isHidden = comment.isHidden(); // 현재 사용자가 댓글을 볼 수 없는지 여부
        if (user != null) {
            if (user.isAdmin()) isHidden = false;
            if (comment.getUser() != null && comment.getUser().getId().equals(user.getId())) isHidden = false;
            if (post.getUser().getId().equals(user.getId())) isHidden = false;
        }
        return ResponseCommentDto.builder()
                .id(comment.getId())
                .user(comment.getUser() != null ? ResponseUserDto.of(comment.getUser()) : ResponseUserDto.of(comment.getGuest().getUsername()))
                .parent(comment.getParent())
                .content(!isHidden ? comment.getContent() : "비밀 댓글입니다.")
                .createdAt(comment.getCreatedAt())
                .hidden(comment.isHidden()) // 비밀댓글 체크 여부
                .hiddenFlag(isHidden)
                .deleted(comment.isDeleted())
                // comment의 user가 null이면, 익명댓글로 항상 true
                .ownership(comment.getUser() == null || comment.getUser().getEmail().equals(email))
                .build();
    }
}
