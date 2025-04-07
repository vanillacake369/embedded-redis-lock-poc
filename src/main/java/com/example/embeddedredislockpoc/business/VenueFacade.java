package com.example.embeddedredislockpoc.business;

import com.example.embeddedredislockpoc.infra.RedisHashesOperator;
import com.example.embeddedredislockpoc.model.Seat;
import com.example.embeddedredislockpoc.model.Venue;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VenueFacade {

    private final RedisHashesOperator redisHashesOperator;

    public void save(Venue venue, Long timeToLive) {
        String key = venue.venueId();
        Map<String, String> fieldValues = venue.seats()
            .stream()
            .collect(Collectors.toMap(
                Seat::ordererId,
                Seat::seatId
            ));
        List<String> seatIds = venue.seats()
            .stream()
            .map(Seat::ordererId)
            .toList();
        redisHashesOperator.putAll(key, fieldValues);
        redisHashesOperator.setTTL(key, timeToLive, seatIds);
    }
}
