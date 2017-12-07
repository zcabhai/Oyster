package com.tfl.billing;

import com.oyster.*;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;

import java.math.BigDecimal;
import java.util.*;

public class TravelTracker implements ScanListener {

    static final BigDecimal OFF_PEAK_JOURNEY_PRICE_LONG = new BigDecimal(2.70);
    static final BigDecimal OFF_PEAK_JOURNEY_PRICE_SHORT = new BigDecimal(1.60);
    static final BigDecimal PEAK_JOURNEY_PRICE_LONG = new BigDecimal(3.80);
    static final BigDecimal PEAK_JOURNEY_PRICE_SHORT = new BigDecimal(2.90);
    static final BigDecimal PEAK_CAP = new BigDecimal(9);
    static final BigDecimal OFF_PEAK_CAP = new BigDecimal(7);

    private final List<JourneyEvent> eventLog;
    private final Set<UUID> currentlyTravelling;
    private final Database customerDatabase;
    private final ClockInterface systemClock;
    private final PaymentsSystemInterface paymentSystem;

    public TravelTracker()
    {
    	this.eventLog = new ArrayList<JourneyEvent>();
    	this.currentlyTravelling = new HashSet<UUID>();
    	this.customerDatabase = new CustomerDatabaseAdapter(CustomerDatabase.getInstance());
    	this.systemClock = new Clock();
    	this.paymentSystem = new PaymentSystemAdapter(PaymentsSystem.getInstance());
    }

    // Different constructor with parameters- used for setting values (mainly passing mock objects as parameters) for testing purposes
    public TravelTracker(ArrayList<JourneyEvent> eventLogValue, Set<UUID> currentlyTravellingValue, Database customerDatabaseValue, ClockInterface clockValue, PaymentsSystemInterface paymentSystemObject)
    {
        this.eventLog = eventLogValue;
        this.currentlyTravelling = currentlyTravellingValue;
        this.customerDatabase  = customerDatabaseValue;
        this.systemClock = clockValue;
        this.paymentSystem = paymentSystemObject;
    }

    public void chargeAccounts() {
        List<Customer> customers = customerDatabase.getCustomers();
        for (Customer customer : customers) {
            totalJourneysFor(customer);
        }
    }



    private void totalJourneysFor(Customer customer) {
        List<JourneyEvent> customerJourneyEvents = createCustomerJourneyEvents(customer);
        List<Journey> journeys = determineJourneys(customerJourneyEvents);
        BigDecimal customerTotal = calculateCustomerTotal(journeys);

        //paymentSystem.charge(customer, journeys, roundToNearestPenny(customerTotal));
    }
    
    private List<JourneyEvent> createCustomerJourneyEvents(Customer customer){
    	List<JourneyEvent> customerJourneyEvents = new ArrayList<JourneyEvent>();
        for (JourneyEvent journeyEvent : eventLog) {
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }
        return customerJourneyEvents;
    }
    
    private List<Journey> determineJourneys(List<JourneyEvent> customerJourneyEvents) {
        List<Journey> journeys = new ArrayList<Journey>();

        JourneyEvent start = null;
        for (JourneyEvent event : customerJourneyEvents) {
            if (event instanceof JourneyStart) {
                start = event;
            }
            if (event instanceof JourneyEnd && start != null) {
                journeys.add(new Journey(start, event));
                start = null;
            } 
        }
        return journeys;
    }
    
    private BigDecimal calculateCustomerTotal(List<Journey> journeys){
        BigDecimal customerTotal = new BigDecimal(0);
        BigDecimal journeyPrice;
        int peakJourneyUsed = 0;
        for (Journey journey : journeys) {
            journeyPrice = calculateJourneyPrice(journey);
            if (peakJourneyUsed == 0){
            	if (checkPeak(journey)) {
                	peakJourneyUsed = 1;
                }
            }
            customerTotal = customerTotal.add(journeyPrice);
        }
        return capCustomerTotal(customerTotal, peakJourneyUsed);

    }
    
    private BigDecimal capCustomerTotal(BigDecimal customerTotal, int peakJourneyUsed){
        if (peakJourneyUsed == 1){
        	if (customerTotal.doubleValue() > PEAK_CAP.doubleValue()){
        		return PEAK_CAP;
        	}
        	else {
        		return roundToNearestPenny(customerTotal);
        	}
        }
        else{
        	if (customerTotal.doubleValue() > OFF_PEAK_CAP.doubleValue()){
        		return OFF_PEAK_CAP;
        	}
        	else {
        		return roundToNearestPenny(customerTotal);
        	}
        }
    }
    
    private boolean checkPeak(Journey journey){
    	if (peak(journey)){
    		return true;
    	}
    	return false;
    }
    
    
    private BigDecimal calculateJourneyPrice(Journey journey) {
    	if (peak(journey)){
    		if (isLong(journey)) {
    			return PEAK_JOURNEY_PRICE_LONG;
    		}
    		else{
    			return PEAK_JOURNEY_PRICE_SHORT;
    		}
    	}
    	else{
    		if (isLong(journey)) {
    			return OFF_PEAK_JOURNEY_PRICE_LONG;
    		}
    		else{
    			return OFF_PEAK_JOURNEY_PRICE_SHORT;
    		}
    	}
    }
    
    private boolean isLong(Journey journey) {
    	if (journey.durationSeconds()/60>25) {
    		return true;
    	}
    	return false;
    }
    

    private BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private boolean peak(Journey journey) {
        return peak(journey.startTime()) || peak(journey.endTime());
    }

    private boolean peak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 19);
    }

    public void connect(OysterCardReader... cardReaders) {
        for (OysterCardReader cardReader : cardReaders) {
            cardReader.register(this);
        }
    }

    @Override
    public void cardScanned(UUID cardId, UUID readerId) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId, systemClock));
            currentlyTravelling.remove(cardId);
        } else {
            if (CustomerDatabase.getInstance().isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId, systemClock));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

}
