package com.tfl.billing;

import com.tfl.external.Customer;
import com.tfl.external.PaymentsSystem;
import com.tfl.underground.OysterReaderLocator;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PaymentSystemAdapter implements PaymentsSystemInterface {

    private PaymentsSystem paymentSystem;
    private PaymentsSystem instance = PaymentsSystem.getInstance();

    public PaymentSystemAdapter(PaymentsSystem paymentSystem)
    {
        this.paymentSystem = paymentSystem;
    }

    @Override
    public String stationWithReader(UUID originId) {
        return OysterReaderLocator.lookup(originId).name();
    }

    @Override
    public PaymentsSystem getInstance() {return instance;}

    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal totalBill) {}
}
