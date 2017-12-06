package com.tfl.billingtests;

import static org.junit.Assert.*;
import com.tfl.billing.*;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.jmock.Expectations;

import java.text.DateFormat;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JourneyTest {

    UUID JourneyStartCardId = UUID.randomUUID();
    UUID JourneyStartReaderId = UUID.randomUUID();
    UUID JourneyEndCardId = UUID.randomUUID();
    UUID JourneyEndReaderId = UUID.randomUUID();

    JourneyStart ExJourneyStart = new JourneyStart(JourneyStartCardId, JourneyStartReaderId);
    JourneyEnd ExJourneyEnd = new JourneyEnd(JourneyEndCardId, JourneyEndReaderId);
    Journey ExJourney = new Journey(ExJourneyStart, ExJourneyEnd);

    @Test
    public void OriginIdTest()
    {
        assertThat(ExJourney.originId(), is(ExJourneyStart.readerId()));
    }

    @Test
    public void DestinationIdTest()
    {
        assertThat(ExJourney.destinationId(), is(ExJourneyEnd.readerId()));
    }

    @Test
    public void StartTimeTest()
    {
        assertThat(ExJourney.startTime(), is(new Date(ExJourneyStart.time())));
    }

    @Test
    public void EndTimeTest()
    {
        assertThat(ExJourney.endTime(), is(new Date(ExJourneyEnd.time())));
    }

    @Test
    public void FormattedStartTimeTest()
    {
        assertThat(ExJourney.formattedStartTime(), is(SimpleDateFormat.getInstance().format(new Date(ExJourneyStart.time()))));
    }

    @Test
    public void FormattedEndTimeTest()
    {
        assertThat(ExJourney.formattedEndTime(), is(SimpleDateFormat.getInstance().format(new Date(ExJourneyEnd.time()))));
    }

    @Test
    public void DurationSecondsTest()
    {
        assertThat(ExJourney.durationSeconds(), is((int) (ExJourneyEnd.time() - ExJourneyStart.time()) / 1000));
    }

    @Test
    public void DurationMinutesTest()
    {
        assertThat(ExJourney.durationMinutes(), is( "" + ExJourney.durationSeconds() / 60 + ":" + ExJourney.durationSeconds() % 60));
    }
}