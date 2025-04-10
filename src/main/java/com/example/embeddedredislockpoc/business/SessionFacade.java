package com.example.embeddedredislockpoc.business;

import com.example.embeddedredislockpoc.infra.RedisHashesOperator;
import com.example.embeddedredislockpoc.model.Seat;
import com.example.embeddedredislockpoc.model.Session;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionFacade {

    private final RedisHashesOperator redisHashesOperator;

    public void save(Session session, Long timeToLive) {
        String key = session.venueId();
        Map<String, String> fieldValues = session.seats()
            .stream()
            .collect(Collectors.toMap(
                Seat::ordererId,
                Seat::seatId
            ));
        List<String> seatIds = session.seats()
            .stream()
            .map(Seat::ordererId)
            .toList();
        redisHashesOperator.putAll(key, fieldValues);
        redisHashesOperator.setTTL(key, timeToLive, seatIds);
    }
}
