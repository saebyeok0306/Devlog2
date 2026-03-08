package io.blog.devlog.domain.post.model;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDetail {
    private Post post;
    private boolean commentFlag; // 댓글 작성 가능 여부
}
