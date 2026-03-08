package io.blog.gateway.global.filter;

import io.blog.gateway.authentication.filter.AuthenticationProcessingFilter;
import io.blog.gateway.global.config.LoggerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<LoggerConfig> {
    private final List<String> ignoreUrls = List.of("/main/signout", "/main/reissue");
    @Autowired
    private final AuthenticationProcessingFilter authenticationProcessingFilter;

    public GlobalFilter(AuthenticationProcessingFilter authenticationProcessingFilter) {
        super(LoggerConfig.class);
        this.authenticationProcessingFilter = authenticationProcessingFilter;
    }

    @Override
    public GatewayFilter apply(LoggerConfig loggerConfig) {
        return (exchange, chain) -> {
            log.info("Message : {}", loggerConfig.getBaseMessage());
            log.info("---------------------------------------------------------");
            if(loggerConfig.isPreLogger()) {
                log.info("Start : {}", exchange.getRequest());
            }

            log.info("Path : {}", exchange.getRequest().getURI().getPath());
            if (!ignoreUrls.contains(exchange.getRequest().getURI().getPath())) {
                Mono<Void> authenticationFilter = authenticationProcessingFilter.doFilterInternal(exchange);
                if (authenticationFilter != null) {
                    return authenticationFilter;
                }
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(loggerConfig.isPostLogger()) {
                    log.info("End : {}  Status : {}", exchange.getResponse(), exchange.getResponse().getStatusCode());
                    log.info("{}", exchange.getResponse().getHeaders());
                }
            }));
        };
    }
}
