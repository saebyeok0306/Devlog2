package io.blog.gateway.global.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String path;

    @JsonIgnore
    private final ObjectMapper objectMapper;

    protected byte[] toJsonBytes() {
        try {
            return objectMapper.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public Mono<Void> setResponse(ServerWebExchange exchange, int status, String error) {
        log.error("ERROR RESPONSE  status: {}  message: {}", status, error);
        ServerHttpResponse response = exchange.getResponse();
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date());
        this.status = status;
        this.error = error;
        // Gateway에서는 어떤 서비스로 향하는 Request인지를 나타내는 항목(index 0)이 있기 때문에 제외하고 합침.
        // ex. /path1/path2 -> {"", "path1", "path2"} 이기 때문에 공백과 첫번째 항목을 지워서 index가 2부터 시작함.
        String[] paths = exchange.getRequest().getURI().getPath().split("/");
        path = "/" + Arrays.stream(paths, 2, paths.length).collect(Collectors.joining("/"));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setRawStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(this.toJsonBytes());
        return response.writeWith(Flux.just(buffer));
    }
}