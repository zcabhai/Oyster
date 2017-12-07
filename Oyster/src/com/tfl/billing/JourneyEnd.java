package com.tfl.billing;

import java.util.UUID;

public class JourneyEnd extends JourneyEvent {

    public JourneyEnd(UUID cardId, UUID readerId, ClockInterface SystemClock) {
        super(cardId, readerId, SystemClock);
    }
}
