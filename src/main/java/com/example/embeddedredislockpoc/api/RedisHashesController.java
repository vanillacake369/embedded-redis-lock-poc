package com.example.embeddedredislockpoc.api;

import com.example.embeddedredislockpoc.business.VenueFacade;
import com.example.embeddedredislockpoc.infra.RedisHashesOperator;
import com.example.embeddedredislockpoc.model.Seat;
import com.example.embeddedredislockpoc.model.Venue;
import java.util.List;
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
    private final VenueFacade venueFacade;

    @PostMapping("/hash/add")
    public ResponseEntity<?> addHash() {
        String venueId = "venue-1";
        List<Seat> seats = List.of(
            new Seat("orderer-1", "floor1-section1-row1-1"),
            new Seat("orderer-2", "floor1-section1-row1-2"),
            new Seat("orderer-3", "floor1-section1-row1-3"),
            new Seat("orderer-4", "floor1-section1-row1-4")
        );
        Venue venue = new Venue(venueId, seats);
        Long timeToLive = 5L;
        venueFacade.save(venue,timeToLive);
        return ResponseEntity.ok("Added reserved seats");
    }
}
