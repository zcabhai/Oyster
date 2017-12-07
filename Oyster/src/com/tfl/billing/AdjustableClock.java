package com.tfl.billing;

public class AdjustableClock implements ClockInterface
{
    private long time = System.currentTimeMillis();

    public void changeTime(long timeValue)
    {
        time = timeValue;
    }

    public void addTime(long timeValue)
    {
        time = time + timeValue;
    }

    public void setTime(long timeValue)
    {
        changeTime(timeValue);
        getCurrentTime();
    }

    @Override
    public long getCurrentTime()
    {
        return time;
    }
}
