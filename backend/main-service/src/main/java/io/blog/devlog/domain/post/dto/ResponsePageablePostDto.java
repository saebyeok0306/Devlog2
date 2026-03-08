package io.blog.devlog.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePageablePostDto {
    List<ResponsePostNonContentDto> posts;
    private int totalPages;
    private int currentPage;
    private long totalElements;
}
