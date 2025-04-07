package com.example.embeddedredislockpoc.model;

import java.util.Map;
import lombok.Getter;

public final class VenueReservation {

    @Getter
    private static final String venueId = "venue-1";

    private static final Map<String, String> reservedSeats = Map.of(
        "orderer-1", "floor1-section1-row1-1",
        "orderer-2", "floor1-section1-row1-2",
        "orderer-3", "floor1-section1-row1-3",
        "orderer-4", "floor1-section1-row1-4"
    );

    @Getter
    private static final Long timeToLive = 5L;

    public Map<String, String> getReservedSeats() {
        return Map.copyOf(reservedSeats);
    }

}
