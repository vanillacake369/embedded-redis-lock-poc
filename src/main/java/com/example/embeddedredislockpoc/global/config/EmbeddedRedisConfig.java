package com.example.embeddedredislockpoc.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmbeddedRedisConfig {

    private final static int REDIS_SERVER_MAX_MEMORY = 128;
    private final static String REDIS_SERVER_MAX_MEMORY_SETTING = String.format("maxmemory %dM", REDIS_SERVER_MAX_MEMORY);
    private RedisProperties redisProperties;
    private RedisServer redisServer;

    /**
     * REDIS_SERVER_MAX_MEMORY : "maxmemory 128M"
     */
    @PostConstruct
    public void startRedis() throws IOException {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost("localhost");
        redisProperties.setPort(14000);
        this.redisProperties = redisProperties;

        int port = this.redisProperties.getPort();
        redisServer = RedisServer.newRedisServer()
            .port(port)
            .setting(REDIS_SERVER_MAX_MEMORY_SETTING)
            .build();

        redisServer.start();
    }

    @PreDestroy
    public void stop() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }

//    @Bean
//    public RedisTemplate<String, VerifyNumber> reactiveRedisTemplate() {
//
//        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
//            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//
//        Jackson2JsonRedisSerializer<VerifyNumber> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, VerifyNumber.class);
//
//        RedisSerializationContext<String, VerifyNumber> serializationContext =
//            RedisSerializationContext.<String, VerifyNumber>newSerializationContext(new StringRedisSerializer())
//                .value(valueSerializer)
//                .build();
//
//        return new RedisTemplate<>(redisConnectionFactory(), serializationContext);
//    }
}