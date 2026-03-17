package io.blog.devlog.domain.user.dto;

import io.blog.devlog.domain.file.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestProfileUrlDto {
    private FileDto file;
    private String profileUrl;
}
