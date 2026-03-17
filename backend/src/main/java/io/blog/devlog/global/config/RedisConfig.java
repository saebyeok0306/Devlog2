package io.blog.devlog.global.config;

import io.blog.devlog.domain.mail.service.CommentEmailSubService;
import io.blog.devlog.domain.mail.service.VerifyEmailSubService;
import io.blog.devlog.domain.sitemap.service.SitemapService;
import io.blog.devlog.global.redis.message.VerifyEmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;
    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    private final SitemapService sitemapService;
    private final VerifyEmailSubService verifyEmailSubService;
    private final CommentEmailSubService commentEmailSubService;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("Redis Host: {}", redisHost);
        log.info("Redis Port: {}", redisPort);
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    // redisTemplate 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(VerifyEmailMessage.class));
        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter sitemapMessageListenerAdapter() {
        return new MessageListenerAdapter(sitemapService, "handleMessage");
    }

    @Bean
    public MessageListenerAdapter verifyEmailMessageListenerAdapter() {
        return new MessageListenerAdapter(verifyEmailSubService);
    }

    @Bean
    public MessageListenerAdapter commentEmailMessageListenerAdapter() {
        return new MessageListenerAdapter(commentEmailSubService);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(sitemapMessageListenerAdapter(), new ChannelTopic("sitemap-topic"));
        container.addMessageListener(verifyEmailMessageListenerAdapter(), new ChannelTopic("verify-email-topic"));
        container.addMessageListener(commentEmailMessageListenerAdapter(), new ChannelTopic("comment-email-topic"));
        return container;
    }
}
