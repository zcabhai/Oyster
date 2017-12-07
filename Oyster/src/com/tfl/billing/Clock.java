package com.tfl.billing;

public class Clock implements ClockInterface {
    @Override
    public long getCurrentTime()
    {
        return System.currentTimeMillis();
    }
}
