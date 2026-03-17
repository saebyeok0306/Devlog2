package io.blog.devlog.domain.file.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityType {
    USER("TYPE_USER"),
    POST("TYPE_POST"),
    COMMENT("TYPE_COMMENT"),
    CATEGORY("TYPE_CATEGORY");
    private final String key;
}
