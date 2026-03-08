package io.blog.devlog.domain.like.model;

import io.blog.devlog.domain.user.dto.ResponseUserDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostLikeDetail {
    private int likeCount;
    private List<ResponseUserDto> users;
    private boolean isLiked;
}
