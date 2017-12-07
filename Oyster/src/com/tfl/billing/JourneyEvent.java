package com.tfl.billing;

import java.util.UUID;

public abstract class JourneyEvent{

    private final UUID cardId;
    private final UUID readerId;
    private final long time;

    public JourneyEvent(UUID cardId, UUID readerId) {
        this.cardId = cardId;
        this.readerId = readerId;
        this.time = System.currentTimeMillis();
    }

    // different constructor for new implementation
    public JourneyEvent(UUID cardId, UUID readerId, ClockInterface SystemClock) {
        this.cardId = cardId;
        this.readerId = readerId;
        this.time = SystemClock.getCurrentTime();
    }

    public UUID cardId() {
        return cardId;
    }

    public UUID readerId() {
        return readerId;
    }

    public long time() {
        return time;
    }
}