package io.blog.devlog.domain.comment.dto;

import io.blog.devlog.domain.file.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestEditCommentDto {
    private String content;
    @Builder.Default
    private List<FileDto> files = Collections.emptyList();
    @ColumnDefault("false")
    private boolean hidden;
}
