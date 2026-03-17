package io.blog.devlog.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCategoryDto {
    private Long id;
    private long layer; // 카테고리 순서
    private String name;
    private boolean commentFlag; // 댓글 작성 가능 여부
}
