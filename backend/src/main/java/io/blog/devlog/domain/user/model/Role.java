package io.blog.devlog.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST(0, "ROLE_GUEST"),
    USER(100, "ROLE_USER"),
    PARTNER(900, "ROLE_PARTNER"),
    ADMIN(1000, "ROLE_ADMIN");
    private final int key;
    private final String nameKey;

    public static Role fromKey(int key) {
        for (Role role : Role.values()) {
            if (role.getKey() == key) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid integer key: " + key);
    }

    public static Role fromNameKey(String nameKey) {
        for (Role role : Role.values()) {
            if (role.getNameKey().equals(nameKey)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid string key: " + nameKey);
    }
}
