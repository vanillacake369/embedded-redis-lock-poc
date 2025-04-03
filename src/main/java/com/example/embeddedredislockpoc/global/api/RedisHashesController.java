package com.example.embeddedredislockpoc.global.api;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisHashesController {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/hash/add")
    public ResponseEntity<?> addHash() {
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        String venueId = "venue-1";
        Map<String, String> reservedSeats = Map.of(
            "orderer-1", "floor1-section1-row1-1",
            "orderer-2", "floor1-section1-row1-2",
            "orderer-3", "floor1-section1-row1-3",
            "orderer-4", "floor1-section1-row1-4"
        );
        stringObjectObjectHashOperations.putAll(venueId, reservedSeats);
        return ResponseEntity.ok("Added reserved seats");
    }
}
