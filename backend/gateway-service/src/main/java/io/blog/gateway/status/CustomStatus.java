package io.blog.gateway.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomStatus {
    EXPIRED_JWT(900),
    INVALID_JWT(901);

    private final int value;
}
