package com.example.embeddedredislockpoc.api;

import com.example.embeddedredislockpoc.infra.RedisHashesOperator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisHashesController {

    private final HashOperations<String, Object, Object> hashOperations;
    private final RedisHashesOperator redisHashesOperator;

    @PostMapping("/hash/add")
    public ResponseEntity<?> addHash() {
        String venueId = "venue-1";
        Map<String, String> reservedSeats = Map.of(
            "orderer-1", "floor1-section1-row1-1",
            "orderer-2", "floor1-section1-row1-2",
            "orderer-3", "floor1-section1-row1-3",
            "orderer-4", "floor1-section1-row1-4"
        );
        Long timeToLive = 5L;

        hashOperations.putAll(venueId, reservedSeats);
        List<String> fieldNames = List.of("orderer-3", "orderer-4");
        redisHashesOperator.setTTL(venueId, timeToLive, fieldNames);

        return ResponseEntity.ok("Added reserved seats");
    }
}
