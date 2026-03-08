package io.blog.devlog.global.manager;

import io.blog.devlog.global.login.dto.PrincipalDetails;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class MinimumRoleKeyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final int requiredKey;

    public MinimumRoleKeyAuthorizationManager(int requiredKey) {
        this.requiredKey = requiredKey;
    }


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();

        if (auth.getPrincipal() instanceof PrincipalDetails principalDetails) {
            return new AuthorizationDecision(principalDetails.getRoleKey() >= requiredKey);
        }

        return new AuthorizationDecision(false);
    }
}
