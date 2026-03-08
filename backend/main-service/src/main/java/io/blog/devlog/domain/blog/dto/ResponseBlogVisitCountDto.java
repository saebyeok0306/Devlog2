package io.blog.devlog.domain.blog.dto;

import io.blog.devlog.domain.views.dto.PostViewCountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBlogVisitCountDto {
    private Long totalCount;
    private List<PostViewCountDto> visitCounts;
}
