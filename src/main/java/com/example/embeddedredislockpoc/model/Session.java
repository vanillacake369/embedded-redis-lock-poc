package com.example.embeddedredislockpoc.model;

import java.util.List;

public record Session(
    String venueId,
    List<Seat> seats
) {

}
