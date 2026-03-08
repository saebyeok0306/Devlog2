package io.blog.gateway.global.filter;

import io.blog.gateway.global.config.LoggerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class MainFilter  extends AbstractGatewayFilterFactory<LoggerConfig> {

    public MainFilter() {
        super(LoggerConfig.class);
    }

    @Override
    public GatewayFilter apply(LoggerConfig loggerConfig) {
        return (exchange, chain) -> {
            log.info("Message : {}", loggerConfig.getBaseMessage());
            log.info("---------------------------------------------------------");
            if(loggerConfig.isPreLogger()) {
                log.info("Start : {}", exchange.getRequest());
            }


            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(loggerConfig.isPostLogger()) {
                    log.info("End : {}  Status : {}", exchange.getResponse(), exchange.getResponse().getStatusCode());
                }
            }));
        };
    }
}
