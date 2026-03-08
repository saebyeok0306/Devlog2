package io.blog.devlog.global.utils;

import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.login.dto.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;

public class SecurityUtils {

    public static Role getPrincipalRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Role.GUEST;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            return ((PrincipalDetails) principal).getUser().getRole();
        }
        return null;
    }

    public static String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((PrincipalDetails) principal).getUsername();
            if (Objects.equals(username, "GUEST")) return null;
            return username;
        }
        return null;
    }

    public static Optional<User> getUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            return Optional.of(((PrincipalDetails) principal).getUser());
        }
        return Optional.empty();
    }
}
