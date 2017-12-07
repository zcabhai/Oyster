package com.tfl.billing;

import com.tfl.external.Customer;
import com.tfl.external.PaymentsSystem;
import com.tfl.underground.OysterReaderLocator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentsSystemInterface {

    // PaymentsSystem instance = new PaymentsSystem();
    void charge(Customer customer, List<Journey> journeys, BigDecimal totalBill);
    PaymentsSystem getInstance();
    public String stationWithReader(UUID originId);
}
