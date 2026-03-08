package io.blog.devlog.global.login.service;

import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.login.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// 시큐리티 설정에서 loginProcessingUrl("/login")에 의해 login 요청이 오면 자동으로 UserDetailsService 타입으로
// IoC 되어 있는 loadUserByUsername 함수가 실행됨!
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService의 loadUserByUsername({}) 호출됨", email);
        Optional<User> userEntity = userService.getUserByEmail(email);
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            return new PrincipalDetails(user);
        }
        throw new UsernameNotFoundException(email + " -> 없는 계정입니다.");
    }
}
