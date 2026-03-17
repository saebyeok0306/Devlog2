package io.blog.devlog.domain.views.dto;

import io.blog.devlog.domain.post.dto.ResponsePostNonContentDto;
import io.blog.devlog.domain.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostViewCountDto {
    private ResponsePostNonContentDto post;
    private List<PostViewCountDto> viewCounts;

    public static ResponsePostViewCountDto of(Post post, List<PostViewCountDto> postViewCountDtoList) {
        return ResponsePostViewCountDto.builder()
                .post(ResponsePostNonContentDto.of(post))
                .viewCounts(postViewCountDtoList)
                .build();
    }
}
