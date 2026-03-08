package io.blog.devlog.domain.user.service;

import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.repository.UserRepository;
import io.blog.devlog.global.jwt.service.JwtService;
import io.blog.devlog.utils.EntityFactory;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 추가")
    void saveUser() {
        // given
        User user = EntityFactory.createUser();
        given(userRepository.save(user)).willReturn(user);

        // when
        User user1 = userService.saveUser(user);

        // then
        Assertions.assertThat(user1).isInstanceOf(User.class);
        Assertions.assertThat(user1.getEmail()).isEqualTo(user.getEmail());
    }
}
