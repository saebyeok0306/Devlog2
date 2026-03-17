package io.blog.devlog.domain.user.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        // Entity -> DB
        return role != null ? role.getKey() : null;
    }

    @Override
    public Role convertToEntityAttribute(Integer key) {
        // DB -> Entity
        return key != null ? Role.fromKey(key) : null;
    }
}
