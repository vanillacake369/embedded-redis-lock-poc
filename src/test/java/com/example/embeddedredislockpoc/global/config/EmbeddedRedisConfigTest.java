package com.example.embeddedredislockpoc.global.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class EmbeddedRedisConfigTest {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    @DisplayName("임베디드 레디스 연결 시 성공합니다")
    void 임베디드레디스연결시성공합니다() {
        // GIVEN
        // WHEN
        RedisConnection connection = redisConnectionFactory.getConnection();

        // THEN
        String ping = connection.ping();
        assertNotNull(ping);
        assertEquals("pong", ping.toLowerCase());
    }

}