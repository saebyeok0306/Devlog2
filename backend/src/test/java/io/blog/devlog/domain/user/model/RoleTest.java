package io.blog.devlog.domain.user.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RoleTest {

    @Test
    @DisplayName("Role fromNameKey 테스트")
    public void fromNameKeyTest() {
        // given
        List<String> nameKeys = new ArrayList<>();
        for (Role role : Role.values()) {
            nameKeys.add(role.getNameKey());
        }

        // when
        List<Role> roles = new ArrayList<>();
        for (String nameKey : nameKeys) {
            roles.add(Role.fromNameKey(nameKey));
        }

        // then
        for (int i=0; i<roles.size(); i++) {
            Assertions.assertThat(roles.get(i).getNameKey()).isEqualTo(nameKeys.get(i));
        }
    }
}
