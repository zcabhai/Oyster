package com.tfl.billingtests;

import com.tfl.billing.Clock;
import com.tfl.billing.ClockInterface;
import com.tfl.billing.JourneyEnd;
import org.junit.Test;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyEndTest {

    UUID cardID = UUID.randomUUID();
    UUID readerID = UUID.randomUUID();
    ClockInterface testClock = new Clock();

    JourneyEnd TestJourneyEnd = new JourneyEnd(cardID, readerID, testClock);

    @Test
    public void MatchCardIdTest()
    {
        assertThat(TestJourneyEnd.cardId(), is(cardID));
    }

    @Test
    public void MatchReaderIdTest()
    {
        assertThat(TestJourneyEnd.readerId(), is(readerID));
    }

    @Test
    public void TimeTest()
    {
        // check that the time works
    }
}