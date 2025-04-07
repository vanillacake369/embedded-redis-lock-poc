package com.example.embeddedredislockpoc.model;

import java.util.List;

public record Venue(
    String venueId,
    List<Seat> seats
) {

}
