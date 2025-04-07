package com.example.embeddedredislockpoc.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("embedded")
public class EmbeddedRedisConfig {


    private final static int REDIS_SERVER_MAX_MEMORY = 128;
    private final static String REDIS_SERVER_MAX_MEMORY_SETTING = String.format("maxmemory %dM", REDIS_SERVER_MAX_MEMORY);
    private final RedisProperties redisProperties;
    private RedisServer redisServer;

    /**
     * REDIS_SERVER_MAX_MEMORY : "maxmemory 128M"
     */
    @PostConstruct
    public void startRedis() throws IOException {
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
}