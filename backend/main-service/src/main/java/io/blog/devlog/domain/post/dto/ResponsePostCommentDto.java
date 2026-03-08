package io.blog.devlog.domain.post.dto;

import io.blog.devlog.domain.comment.dto.ResponseCommentDto;
import io.blog.devlog.domain.like.model.PostLikeDetail;
import io.blog.devlog.domain.post.model.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostCommentDto {
    private ResponsePostDto post;
    private List<ResponseCommentDto> comments;
    private boolean commentFlag;
    private PostLikeDetail likes;

    public static ResponsePostCommentDto of(String email, PostDetail postDetail, List<ResponseCommentDto> comments, PostLikeDetail postLikeDetail) {
        return ResponsePostCommentDto.builder()
                .post(ResponsePostDto.of(email, postDetail.getPost()))
                .comments(comments)
                .commentFlag(postDetail.isCommentFlag())
                .likes(postLikeDetail)
                .build();
    }
}
