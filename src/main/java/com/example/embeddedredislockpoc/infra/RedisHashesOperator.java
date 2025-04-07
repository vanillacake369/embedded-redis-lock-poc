package com.example.embeddedredislockpoc.infra;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHashesOperator {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * HEXPIRE ${key} ${ttl seconds} NX FIELDS ${field count} ${field ,,,}
     * @param hashKey Redis Hash Key
     * @param timeToLive TTL (sec)
     * @param fieldNames fields of hash key
     * @apiNote Should not delete try-catch since java.lang.UnsupportedOperationException: io.lettuce.core.output.ByteArrayOutput does not support set(long) is thrown
     */
    public void setTTL(String hashKey, Long timeToLive, List<String> fieldNames) {
        try {
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                byte[] key = hashKey.getBytes(StandardCharsets.UTF_8);
                byte[] seconds = timeToLive.toString().getBytes(StandardCharsets.UTF_8);
                byte[] nx = "NX".getBytes(StandardCharsets.UTF_8);
                byte[] fields = "FIELDS".getBytes(StandardCharsets.UTF_8);
                byte[] numFields = String.valueOf(fieldNames.size()).getBytes(StandardCharsets.UTF_8);
                List<byte[]> args = new ArrayList<>();
                args.add(key);
                args.add(seconds);
                args.add(nx);
                args.add(fields);
                args.add(numFields);
                fieldNames.forEach(name -> args.add(name.getBytes(StandardCharsets.UTF_8)));
                return connection.execute("HEXPIRE", args.toArray(new byte[0][]));
            });
        } catch (Exception ignored) {
        }
    }
}
