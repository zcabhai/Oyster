package com.tfl.billingtests;

import com.tfl.billing.JourneyStart;
import org.junit.Test;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyStartTest {

    // initialise a new JourneyStart with random cardID and random readerID to check if they match
    UUID cardID = UUID.randomUUID();
    UUID readerID = UUID.randomUUID();
    JourneyStart TestJourneyStart = new JourneyStart(cardID, readerID);

    @Test
    public void CardIdTest()
    {
        assertThat(TestJourneyStart.cardId(), is(cardID));
    }

    @Test
    public void ReaderIdTest()
    {
        assertThat(TestJourneyStart.readerId(), is(readerID));
    }

}