package io.blog.devlog.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestGuestEditCommentDto {
    private String content;
    @ColumnDefault("false")
    private boolean hidden;
    private String password;
}
