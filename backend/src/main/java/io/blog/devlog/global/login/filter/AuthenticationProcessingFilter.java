package io.blog.devlog.global.login.filter;

import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.exception.InvalidJwtException;
import io.blog.devlog.global.jwt.service.JwtService;
import io.blog.devlog.global.login.dto.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    private boolean ignoreRequestURI(String requestURI, String[] ignoreURIs) {
        for (String uri : ignoreURIs) {
            if (uri.endsWith("/**")) {
                String _uri = uri.substring(0, uri.length()-4);
                if (requestURI.startsWith(_uri)) {
                    return true;
                }
            }
            else if (requestURI.equals(uri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("AuthenticationProcessingFilter 호출됨 {}", requestURI);

        String[] ignoreURIs = {
            "/actuator/**",
            "/signout",
            "/reissue"
        };

        if (ignoreRequestURI(requestURI, ignoreURIs)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = jwtService.extractJWT(request).orElse(null);
            if (token != null) {
                Claims claims = jwtService.extractClaims(token);
                createUserDetails(claims);
            } else {
                // GUEST
                createUserDetails();
            }
        } catch (JwtException e) {
            throw new InvalidJwtException(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void createUserDetails(Claims claims) throws IOException {
        log.info("saveAuthentication(Claims) 호출");
        User user = userService.getUserByEmail((String) claims.get("email")).orElse(null);
        if (user == null) {
            throw new BadRequestException("User Not Found");
        }
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        saveAuthentication(principalDetails);
    }

    private void createUserDetails() {
        log.info("createUserDetails(GUEST) 호출");
        User user = User.builder().username("GUEST").email("GUEST").build();
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        saveAuthentication(principalDetails);
    }

    private void saveAuthentication(PrincipalDetails userDetails) {
        log.info("saveAuthentication(UserDetails) 호출");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}