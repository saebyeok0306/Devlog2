package io.blog.devlog.domain.file.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    IMAGE("TYPE_IMAGE"),
    VIDEO("TYPE_VIDEO"),
    AUDIO("TYPE_AUDIO"),
    FILES("TYPE_FILES");
    private final String key;
}
