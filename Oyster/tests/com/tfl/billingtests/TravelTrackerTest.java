package com.tfl.billingtests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.PaymentsSystem;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.Customer;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import java.util.*;
import java.math.BigDecimal;


import static org.junit.Assert.*;

public class TravelTrackerTest
{
    private final ArrayList<JourneyEvent> exampleEventLog = new ArrayList<>();
    private final Set<UUID> exampleJourniesTravelling = new HashSet<UUID>();
    private final List<Journey> exampleJournies = new ArrayList<Journey>();
    private OysterCard exampleOyster = new OysterCard("9b481806-daee-11e7-9296-cec278b6b50a");
    OysterCard myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    OysterCardReader paddington = OysterReaderLocator.atStation(Station.PADDINGTON);
    OysterCardReader bakerStreet = OysterReaderLocator.atStation(Station.BAKER_STREET);
    OysterCardReader kingsCross = OysterReaderLocator.atStation(Station.KINGS_CROSS);
    private BigDecimal expectedCost = new BigDecimal(6.70);

    private final AdjustableClock testClock = new AdjustableClock();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Database mockDatabase = context.mock(Database.class);

    private Database exampleCustomerDatabase = new CustomerDatabaseAdapter(CustomerDatabase.getInstance()) ;
    private PaymentsSystemInterface examplePaymentSystem = context.mock(PaymentsSystemInterface.class);

    @Test
    public void checkAddedJourney()
    {
        TravelTracker travelTracker = new TravelTracker(exampleEventLog, exampleJourniesTravelling, exampleCustomerDatabase, testClock, examplePaymentSystem);
        Customer customer1 = new Customer("Billy Howell", myCard);
        testClock.setTime(25000000l);
        paddington.touch(myCard);
        testClock.setTime(30000000l);
        bakerStreet.touch(myCard);

        context.checking(new Expectations(){{
         // allowing(examplePaymentSystem).charge(customer1, exampleJourniesTravelling,);
        }});
       //  travelTracker.chargeAccounts();
       // assertThat();

    }

    @Test
    public void OneCustomerTest()
    {
        TravelTracker travelTracker = new TravelTracker(exampleEventLog, exampleJourniesTravelling, mockDatabase, testClock, examplePaymentSystem);
        List<Customer> mockCustomers = new ArrayList<Customer>();
        Customer customer2 = new Customer("Billy Howell", myCard);
        mockCustomers.add(customer2);


        context.checking(new Expectations(){{
            //ignoring(exampleCustomerDatabase).isRegisteredId(myCard.id()); will(returnValue(true));
            allowing(mockDatabase).isRegisteredId(myCard.id());will(returnValue(true));
            oneOf(mockDatabase).getCustomers();will(returnValue(mockCustomers));
           allowing(examplePaymentSystem).charge(customer2, exampleJournies, expectedCost.setScale(2, BigDecimal.ROUND_HALF_UP));
        }});


        new Customer("Billy Howell", myCard);
        travelTracker.connect(paddington, bakerStreet, kingsCross);

        testClock.setTime(25000000l);
        paddington.touch(myCard);
        testClock.changeTime(30000000l);
        testClock.getCurrentTime();
        bakerStreet.touch(myCard);
        bakerStreet.touch(myCard);
        testClock.changeTime(29200000l);
        testClock.getCurrentTime();
        kingsCross.touch(myCard);
        travelTracker.chargeAccounts();
    }
}