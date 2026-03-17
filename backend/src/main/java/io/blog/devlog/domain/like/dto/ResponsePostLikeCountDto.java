package io.blog.devlog.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostLikeCountDto {
    private String postUrl;
    private int likeCount;
}
