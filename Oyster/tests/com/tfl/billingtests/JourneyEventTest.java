package com.tfl.billingtests;

import com.tfl.billing.*;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.jmock.Expectations;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.UUID;

import static org.hamcrest.core.Is.is;

public class JourneyEventTest {

    UUID cardIdValue = UUID.randomUUID();
    UUID readerIdValue = UUID.randomUUID();

    @Test
    public void CardIdTest ()
    {
        JourneyEvent ExJourneyEvent = new JourneyEvent(cardIdValue, readerIdValue) {};
        assertThat(ExJourneyEvent.cardId(), is(cardIdValue));
    }

    @Test
    public void ReaderIdTest ()
    {
        JourneyEvent ExJourneyEvent = new JourneyEvent(cardIdValue, readerIdValue) {};
        assertThat(ExJourneyEvent.readerId(), is(readerIdValue));
    }


    @Test
    public void TimeTest ()
    {
        JourneyEvent ExJourneyEvent = new JourneyEvent(cardIdValue, readerIdValue) {};
        long TempTime = System.currentTimeMillis();
        assertThat(ExJourneyEvent.time(), is(TempTime));
    }
}